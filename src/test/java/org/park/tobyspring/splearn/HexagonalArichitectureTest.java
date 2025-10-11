package org.park.tobyspring.splearn;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "org.park.tobyspring.splearn", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArichitectureTest {

  @ArchTest
  void hexagonalArchitecture(JavaClasses classes) {
    Architectures.layeredArchitecture()
        .consideringAllDependencies()
        .layer("domain").definedBy("org.park.tobyspring.splearn.domain..")
        .layer("application").definedBy("org.park.tobyspring.splearn.application..")
        .layer("adapter").definedBy("org.park.tobyspring.splearn.adapter..")
        .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
        .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
        .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
        .check(classes);
  }
}
