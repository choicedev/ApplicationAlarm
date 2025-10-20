# 🕐 WakeUp Planner

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Compose](https://img.shields.io/badge/Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)

Um aplicativo de alarme moderno para Android desenvolvido com **Jetpack Compose**, **Room Database** e **Clean Architecture**.

</div>

---

## 📹 Demonstração

<div align="center">

https://github.com/user-attachments/assets/76e8779b-1add-48d9-8576-19aa3ea11983

</div>

---

## ✨ Funcionalidades

| Recurso | Descrição |
|---------|-----------|
| 📱 **Interface Intuitiva** | Criar e editar alarmes com facilidade |
| 🔄 **Alarmes Recorrentes** | Configure para dias específicos da semana |
| 🎵 **Som Customizável** | Selecione tons de alarme do dispositivo |
| ⏰ **Snooze** | Adie o alarme por 5 minutos |
| 🎨 **Material Design 3** | Tema escuro moderno e responsivo |
| 👆 **Swipe to Delete** | Remova alarmes deslizando |
| 🔔 **Notificações** | Controle direto da notificação |
| 📳 **Vibração** | Feedback háptico durante alarme |

---

## 🛠️ Tecnologias

```
┌─ Linguagem
│  └─ Kotlin
│
├─ UI
│  └─ Jetpack Compose + Material Design 3
│
├─ Arquitetura
│  ├─ MVVM
│  └─ Clean Architecture
│
├─ Dados
│  ├─ Room Database
│  └─ TypeConverters (Gson)
│
├─ Async
│  ├─ Coroutines
│  └─ Flow
│
├─ DI
│  └─ Hilt
│
└─ Agendamento
   └─ AlarmManager
```

---

## 📋 Requisitos

| Item | Versão |
|------|--------|
| Android Mínimo | 8.0 (API 27) |
| Android Alvo | 15 (API 35) |
| JDK | 8+ |
| Android Studio | Giraffe+ |

---

## 🚀 Como Executar

<div align="left">

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/wakeup-planner.git
cd wakeup-planner

# 2. Instale a versão debug
./gradlew installDebug

# 3. Ou execute direto no Android Studio
# Run > Run 'app'
```

</div>

---

## 📂 Estrutura do Projeto

```
app/src/main/java/com/choice/wakeuplanner/
│
├── core/                    # Classes base e utilidades
│   ├── Converters.kt
│   ├── IResult.kt
│   ├── UseCase.kt
│   └── helper/
│
├── data/                    # Camada de dados
│   ├── dao/
│   ├── database/
│   ├── entity/
│   ├── mapping/
│   └── repository/
│
├── domain/                  # Lógica de negócio
│   ├── model/
│   ├── repository/
│   └── usecase/
│
├── presentation/            # Camada de UI
│   ├── feature/alarm/
│   ├── feature/alarm_ring/
│   ├── feature/home/
│   ├── theme/
│   └── ui/
│
├── receiver/                # Broadcast Receivers
├── scheduler/               # AlarmManager Implementation
└── service/                 # Foreground Service
```

---

## 🎯 Padrões de Arquitetura

<table>
  <tr>
    <td><strong>Padrão</strong></td>
    <td><strong>Implementação</strong></td>
  </tr>
  <tr>
    <td>🏗️ Arquitetura</td>
    <td>Clean Architecture com MVVM</td>
  </tr>
  <tr>
    <td>📊 State Management</td>
    <td>StateFlow + MutableStateFlow</td>
  </tr>
  <tr>
    <td>📦 Dados</td>
    <td>Repository Pattern</td>
  </tr>
  <tr>
    <td>🔄 Lógica</td>
    <td>Use Cases isolados</td>
  </tr>
  <tr>
    <td>⚠️ Erros</td>
    <td>Sealed Classes (IResult)</td>
  </tr>
  <tr>
    <td>💉 Injeção</td>
    <td>Hilt Dependency Injection</td>
  </tr>
  <tr>
    <td>⏃ Async</td>
    <td>Coroutines + Flow</td>
  </tr>
</table>

---

## 🔐 Permissões Necessárias

```xml
<!-- Alarmes -->
✓ SCHEDULE_EXACT_ALARM
✓ USE_EXACT_ALARM

<!-- Notificações -->
✓ POST_NOTIFICATIONS

<!-- Sistema -->
✓ WAKE_LOCK
✓ FOREGROUND_SERVICE
✓ FOREGROUND_SERVICE_MEDIA_PLAYBACK
✓ USE_FULL_SCREEN_INTENT

<!-- Feedback -->
✓ VIBRATE
```

---

## 📦 Dependências Principais

<details>
<summary><strong>Clique para expandir</strong></summary>

- `androidx.appcompat` - Compatibilidade
- `androidx.activity.compose` - Compose Activity
- `androidx.lifecycle.*` - ViewModel, LiveData
- `androidx.room.*` - Persistência local
- `androidx.navigation.compose` - Navegação
- `androidx.compose.*` - UI Declarativa
- `com.google.dagger.hilt` - Dependency Injection
- `org.jetbrains.kotlinx.coroutines` - Programação assíncrona
- `com.google.code.gson` - Serialização

</details>

---

## 📄 Licença

<div align="center">

**MIT License** - Veja [LICENSE](LICENSE) para detalhes

</div>

---

<div align="center">

**[GitHub](https://github.com/choicedev) • [LinkedIn](https://www.linkedin.com/in/fabriciozamora/)**

</div>
