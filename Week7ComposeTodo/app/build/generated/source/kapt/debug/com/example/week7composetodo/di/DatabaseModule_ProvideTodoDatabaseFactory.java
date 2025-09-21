package com.example.week7composetodo.di;

import android.content.Context;
import com.example.week7composetodo.data.TodoDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideTodoDatabaseFactory implements Factory<TodoDatabase> {
  private final Provider<Context> contextProvider;

  private DatabaseModule_ProvideTodoDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TodoDatabase get() {
    return provideTodoDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideTodoDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideTodoDatabaseFactory(contextProvider);
  }

  public static TodoDatabase provideTodoDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTodoDatabase(context));
  }
}
