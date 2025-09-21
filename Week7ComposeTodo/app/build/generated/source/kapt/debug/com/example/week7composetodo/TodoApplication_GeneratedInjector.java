package com.example.week7composetodo;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = TodoApplication.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface TodoApplication_GeneratedInjector {
  void injectTodoApplication(TodoApplication todoApplication);
}
