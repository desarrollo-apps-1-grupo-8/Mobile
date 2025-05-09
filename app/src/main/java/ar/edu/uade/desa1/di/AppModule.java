package ar.edu.uade.desa1.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    
    @Provides
    @Singleton
    public Context provideApplicationContext(@ApplicationContext Context context) {
        return context;
    }
} 