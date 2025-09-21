package com.example.week7composetodo.data;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class TodoRepository_Factory implements Factory<TodoRepository> {
  private final Provider<TodoDao> todoDaoProvider;

  private TodoRepository_Factory(Provider<TodoDao> todoDaoProvider) {
    this.todoDaoProvider = todoDaoProvider;
  }

  @Override
  public TodoRepository get() {
    return newInstance(todoDaoProvider.get());
  }

  public static TodoRepository_Factory create(Provider<TodoDao> todoDaoProvider) {
    return new TodoRepository_Factory(todoDaoProvider);
  }

  public static TodoRepository newInstance(TodoDao todoDao) {
    return new TodoRepository(todoDao);
  }
}
