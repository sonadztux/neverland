package com.neverland.capstone

import android.app.Application
import com.neverland.capstone.data.network.ApiService
import com.neverland.capstone.data.network.CapstoneEndpoint
import com.neverland.capstone.data.CapstoneRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import timber.log.Timber

class CapstoneApplication : Application(),KodeinAware{
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@CapstoneApplication))

        bind<CapstoneEndpoint>() with singleton { ApiService.getClient() }

        bind() from singleton { CapstoneRepository( instance() ) }

    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}