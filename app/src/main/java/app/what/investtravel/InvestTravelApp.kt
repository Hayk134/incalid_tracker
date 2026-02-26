package app.what.investtravel

import android.app.Application
import android.location.Geocoder
import androidx.room.Room
import app.what.foundation.services.AppLogger
import app.what.foundation.services.AppLogger.Companion.Auditor
import app.what.foundation.services.crash.CrashHandler
import app.what.investtravel.data.local.database.AppDatabase
import app.what.investtravel.data.local.settings.AppValues
import app.what.investtravel.data.remote.AiService
import app.what.investtravel.data.remote.ApiClient
import app.what.investtravel.data.remote.AuthService
import app.what.investtravel.data.remote.PlacesService
import app.what.investtravel.data.remote.MarkersService
import app.what.investtravel.data.remote.ReviewsService
import app.what.investtravel.data.remote.RoutesService
import app.what.investtravel.data.remote.UsersService
import app.what.investtravel.features.assistant.domain.AssistantController
import app.what.investtravel.features.auth.domain.AuthController
import app.what.investtravel.features.dev.presentation.NetworkMonitorPlugin
import app.what.investtravel.features.hotel.domain.HotelController
import app.what.investtravel.features.main.domain.MainController
import app.what.investtravel.features.onboarding.domain.OnboardingController
import app.what.investtravel.features.profile.domain.ProfileController
import app.what.investtravel.features.settings.domain.SettingsController
import app.what.investtravel.features.travel.domain.TravelController
import app.what.investtravel.libs.FileManager
import app.what.investtravel.libs.GoogleDriveParser
import app.what.investtravel.utils.AppUtils
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.MapKitFactory
import com.yandex.runtime.image.ImageProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.security.cert.X509Certificate
import java.util.Locale
import javax.net.ssl.X509TrustManager

class InvestTravelApp : Application() {
    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey("576b91a0-ac5c-421a-a932-38cbe1d4c633")
        MapKitFactory.initialize(applicationContext)
        CrashHandler.initialize(applicationContext)
        AppLogger.initialize(applicationContext)
        Auditor.info("core", "App started")

        startKoin {
            androidContext(this@InvestTravelApp)
            modules(generalModule)
        }
    }
}

val generalModule = module {
    single { AppValues(get()) }
    single { AppUtils(get()) }
    single { GoogleDriveParser(get()) }
    single { FileManager(get()) }

    single { ApiClient(get(), get()) }
    single { AuthService(get(), get()) }
    single { UsersService(get(), get()) }
    single { RoutesService(get(), get()) }
    single { PlacesService(get(), get()) }
    single { MarkersService(get(), get()) }
    single { ReviewsService(get(), get()) }
    single { AiService(get(), get()) }

    single { Geocoder(get(), Locale.getDefault()) }
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }

    single<HotelController> { HotelController(get(), get(), get()) }
    single<ProfileController> { ProfileController(get(), get()) }
    single<AssistantController> { AssistantController() }
    single<AuthController> { AuthController(get(), get(), get()) }
    single<TravelController> { TravelController { ImageProvider.fromResource(androidContext(), it) } }
    single<SettingsController> { SettingsController(get(), get()) }
    single<OnboardingController> { OnboardingController(get()) }
    single<MainController> { MainController() }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "accessible_rostov.db"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<AppDatabase>().routesDao() }
    single { get<AppDatabase>().routePointsDao() }
    single { get<AppDatabase>().placesDao() }
    single { get<AppDatabase>().markersDao() }
    single { get<AppDatabase>().reviewsDao() }

    single {
        HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 60000
            }
            install(NetworkMonitorPlugin)

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Auditor.debug("ktor", message)
                    }
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    classDiscriminator = "type"
                })
            }
            defaultRequest {
                url(ApiClient.BASE_URL)
            }
            engine {
                https {
                    trustManager = object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
                }
            }
        }
    }
}
