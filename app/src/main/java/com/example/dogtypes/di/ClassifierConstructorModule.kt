package com.example.dogtypes.di

import android.content.Context
import com.example.dogtypes.LABEL_PATH
import com.example.dogtypes.MODEL_PATH
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.tensorflow.lite.support.common.FileUtil
import java.nio.MappedByteBuffer

@Module
@InstallIn(SingletonComponent::class)
object ClassifierConstructorModule {

    @Provides
    fun provideClassifierModel(@ApplicationContext context: Context): MappedByteBuffer =
        FileUtil.loadMappedFile(context, MODEL_PATH)

    @Provides
    fun provideClassifierLabels(@ApplicationContext context: Context): List<String> =
        FileUtil.loadLabels(context, LABEL_PATH)
}