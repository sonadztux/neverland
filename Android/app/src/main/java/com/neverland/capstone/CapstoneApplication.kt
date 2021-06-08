package com.neverland.capstone

import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.neverland.capstone.data.network.ApiService
import com.neverland.capstone.data.network.CapstoneEndpoint
import com.neverland.capstone.data.CapstoneRepository
import com.neverland.capstone.viewmodel.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class CapstoneApplication : Application(),KodeinAware{
    override val kodein: Kodein = Kodein.lazy {
        AndroidNetworking.initialize(applicationContext);
        import(androidXModule(this@CapstoneApplication))

        bind<CapstoneEndpoint>() with singleton { ApiService.getClient() }
        bind() from singleton { CapstoneRepository( instance() ) }
        bind() from provider { ViewModelFactory( instance()) }

    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}