package com.loftschool.ozaharenko.loftcoin19.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

class VmFactory implements ViewModelProvider.Factory {

    private final Map<Class<?>, Provider<ViewModel>> providers;

    @Inject
    VmFactory(Map<Class<?>, Provider<ViewModel>> providers) {
        this.providers = providers;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        final Provider<ViewModel> provider = providers.get(modelClass);
        if (provider != null) {
            return (T) provider.get();
        }
        throw new IllegalArgumentException("Unknown model class " + modelClass);
    }

    //more examples to google "Dagger + ViewModel"
}
