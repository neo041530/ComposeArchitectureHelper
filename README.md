# Android Studio Plugin for Feature Creation with Compose and Hilt

This Android Studio plugin helps developers quickly set up a new feature using Compose and Hilt by generating the necessary folder structure and files.

## Features

- Automatically generates the folder structure for a new feature
- Creates essential files including `Route.kt`, `ViewModel.kt`, and data & model-related files
- Streamlines the development process with pre-defined templates

## Usage

1. Right-click on the project or desired directory in the Project view.
2. Select `New` > `CleanArchitectureHelper`.
3. Enter the name of the new feature.
4. The plugin will generate the following folder structure and files:
5. Manually add the generated `Repository` to your Hilt DI Module.

## Generated Folder Structure

```plaintext
<feature-name>/
├── <feature-name>Route.kt
├── <feature-name>ViewModel.kt
├── data/
│   ├── <feature-name>DataSource.kt
│   ├── <feature-name>Repository.kt
│   └── <feature-name>RepositoryImpl.kt
└── model/
    ├── <feature-name>UiState.kt
    └── <feature-name>UiEffect.kt
