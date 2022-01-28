## MVI Flow

Surf MVI Library based on Kotlin Flow

## Connection

![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fartifactory.surfstudio.ru%2Fartifactory%2Flibs-release-local%2Fru%2Fsurfstudio%2Fmvi%2Fmvi-core%2Fmaven-metadata.xml)

```gradle
repositories {
    maven("https://artifactory.surfstudio.ru/artifactory/libs-release-local")
}
dependencies {
    implementation("ru.surfstudio.mvi:mvi-core:${version}")
    implementation("ru.surfstudio.mvi:mvi-flow:${version}")
    implementation("ru.surfstudio.mvi:mvi-flow-lifecycle:${version}")
    implementation("ru.surfstudio.mvi:mvi-mappers:${version}")
}
```

## License

```
Copyright 2022 Surf LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
