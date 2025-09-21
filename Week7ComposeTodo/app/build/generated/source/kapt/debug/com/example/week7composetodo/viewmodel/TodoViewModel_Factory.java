package com.example.week7composetodo.viewmodel;

import com.example.week7composetodo.data.TodoRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class TodoViewModel_Factory implements Factory<TodoViewModel> {
  private final Provider<TodoRepository> repositoryProvider;

  private TodoViewModel_Factory(Provider<TodoRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TodoViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static TodoViewModel_Factory create(Provider<TodoRepository> repositoryProvider) {
    return new TodoViewModel_Factory(repositoryProvider);
  }

  public static TodoViewModel newInstance(TodoRepository repository) {
    return new TodoViewModel(repository);
  }
}
