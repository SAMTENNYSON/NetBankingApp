# Net Banking App

A comprehensive Android banking application built with modern Material Design principles, providing secure and user-friendly banking services on mobile devices.

## 📱 Project Overview

Net Banking App is a full-featured mobile banking application that allows users to manage their bank accounts, perform transactions, view transaction history, and access various banking services through an intuitive and professional user interface.

## ✨ Features

### 🔐 Authentication & Security
- **User Registration**: Secure account registration with validation
- **Login System**: Secure authentication using CIF number and password
- **Transaction PIN (TPIN)**: Additional security layer for transactions
- **Password Management**: Change password functionality
- **TPIN Management**: Change transaction PIN for enhanced security

### 💰 Account Management
- **Account Balance**: Real-time balance display
- **Profile Management**: View and manage account details
  - CIF Number
  - Account Number
  - Branch Code
  - Country
  - Mobile Number
- **Mini Statement**: Quick view of recent transactions on dashboard

### 💸 Financial Operations
- **Fund Transfer**: Secure money transfer between accounts using CIF numbers
- **Transaction History**: Complete history of all transactions
- **Transaction Details**: View transaction type (Debit/Credit), amount, date, and description
- **QR Code Payment**: Generate and scan QR codes for payments

### 📊 Analytics & Insights
- **Transaction Insights**: 
  - Category-wise expense breakdown (Pie Chart)
  - Monthly spending trends (Bar Chart)
  - Total expense calculation
  - Transaction categorization
- **Visual Data Representation**: Interactive charts using MPAndroidChart

### 🧮 Financial Tools
- **EMI Calculator**: Calculate Equated Monthly Installments for loans

### 🎨 User Interface
- **Material Design 3**: Modern, professional UI/UX
- **Card-based Layout**: Clean and organized information display
- **Color-coded Transactions**: Visual distinction between debit (red) and credit (green) transactions
- **Responsive Design**: Optimized for various screen sizes
- **Smooth Animations**: Enhanced user experience with ripple effects and transitions

## 🛠️ Technology Stack

### Core Technologies
- **Language**: Java
- **Platform**: Android (API 24+)
- **Build System**: Gradle (Kotlin DSL)
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 14)
- **Compile SDK**: 36

### Libraries & Dependencies
- **Retrofit 2.9.0**: RESTful API communication
- **Gson Converter**: JSON serialization/deserialization
- **Material Design Components**: Modern UI components
- **ZXing (v4.3.0)**: QR code generation and scanning
- **MPAndroidChart (v3.1.0)**: Data visualization and charts
- **AndroidX Libraries**:
  - AppCompat
  - ConstraintLayout
  - RecyclerView
  - ViewBinding

### Architecture
- **MVC Pattern**: Model-View-Controller architecture
- **RESTful API**: Backend communication via HTTP POST requests
- **ViewBinding**: Type-safe view references

## 📁 Project Structure

```
NetBankingApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/netbankingapp/
│   │   │   │   ├── network/
│   │   │   │   │   ├── ApiClient.java          # Retrofit client configuration
│   │   │   │   │   └── ApiService.java         # API endpoint definitions
│   │   │   │   ├── ui/
│   │   │   │   │   ├── login/
│   │   │   │   │   │   ├── LoginActivity.java
│   │   │   │   │   │   └── LoginValidator.java
│   │   │   │   │   ├── register/
│   │   │   │   │   │   ├── RegisterActivity.java
│   │   │   │   │   │   └── RegisterValidator.java
│   │   │   │   │   ├── dashboard/
│   │   │   │   │   │   ├── DashboardActivity.java
│   │   │   │   │   │   ├── ChangePasswordActivity.java
│   │   │   │   │   │   ├── ChangeTpinActivity.java
│   │   │   │   │   │   └── MiniStatementAdapter.java
│   │   │   │   │   ├── profile/
│   │   │   │   │   │   └── ProfileActivity.java
│   │   │   │   │   ├── transfer/
│   │   │   │   │   │   └── FundTransferActivity.java
│   │   │   │   │   ├── transactions/
│   │   │   │   │   │   ├── TransactionHistoryActivity.java
│   │   │   │   │   │   └── TransactionAdapter.java
│   │   │   │   │   ├── qrcode/
│   │   │   │   │   │   └── QRCodePaymentActivity.java
│   │   │   │   │   ├── insights/
│   │   │   │   │   │   └── TransactionInsightsActivity.java
│   │   │   │   │   └── calculator/
│   │   │   │   │       └── EMICalculatorActivity.java
│   │   │   │   └── res/
│   │   │   │       ├── layout/                 # XML layout files
│   │   │   │       ├── values/
│   │   │   │       │   ├── colors.xml          # Color scheme
│   │   │   │       │   ├── themes.xml          # App theme
│   │   │   │       │   └── strings.xml
│   │   │   │       └── drawable/              # Icons and images
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
|-- netbanking.sql
└── README.md
```

## 🚀 Setup Instructions

### Prerequisites
- Android Studio (Latest version recommended)
- JDK 11 or higher
- Android SDK (API 24+)
- Backend server running PHP with MySQL database
- Android device or emulator

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd NetBankingApp
   ```

2. **Configure Backend URL**
   - Open `app/src/main/java/com/example/netbankingapp/network/ApiClient.java`
   - Update `BASE_URL` with your backend server URL:
     ```java
     private static final String BASE_URL = "http://your-server-ip/";
     ```
   - For Android Emulator, use `http://10.0.2.2/` for localhost
   - For physical device, use your computer's local IP address

3. **Backend Setup**
   - Ensure your backend server is running with the following PHP endpoints:
     - `netbankingapi/login.php`
     - `netbankingapi/register.php`
     - `netbankingapi/get_balance.php`
     - `netbankingapi/get_transactions.php`
     - `netbankingapi/get_mini_statement.php`
     - `netbankingapi/fund_transfer.php`
     - `netbankingapi/change_password.php`
     - `netbankingapi/change_tpin.php`
     - `netbankingapi/get_profile.php`
     - `netbankingapi/get_transaction_insights.php`

4. **Database Setup**
   - Run the SQL scripts provided:
     - `database_add_email_column.sql`
     - `database_create_otp_table.sql`
     - `database_update_add_category.sql`

5. **Build and Run**
   - Open the project in Android Studio
   - Sync Gradle files
   - Connect an Android device or start an emulator
   - Click "Run" or press `Shift + F10`

## 🌐 API Endpoints

### Authentication
- **POST** `/netbankingapi/login.php` - User login
- **POST** `/netbankingapi/register.php` - User registration

### Account Operations
- **POST** `/netbankingapi/get_balance.php` - Get account balance
- **POST** `/netbankingapi/get_profile.php` - Get user profile
- **POST** `/netbankingapi/get_transactions.php` - Get transaction history
- **POST** `/netbankingapi/get_mini_statement.php` - Get recent transactions

### Transactions
- **POST** `/netbankingapi/fund_transfer.php` - Transfer funds

### Security
- **POST** `/netbankingapi/change_password.php` - Change password
- **POST** `/netbankingapi/change_tpin.php` - Change transaction PIN

### Analytics
- **POST** `/netbankingapi/get_transaction_insights.php` - Get transaction insights

## 🎨 UI/UX Design

### Design Principles
- **Material Design 3**: Following Google's latest design guidelines
- **Card-based Layout**: Information organized in cards for better readability
- **Color Scheme**: Professional banking color palette
  - Primary Blue: `#1976D2`
  - Secondary Teal: `#00796B`
  - Success Green: `#4CAF50`
  - Error Red: `#F44336`
  - Transaction Colors: Green (Credit), Red (Debit)

### Key Screens

1. **Login Screen**
   - Clean, centered layout
   - Material TextInputLayout with outlined boxes
   - Professional branding header

2. **Registration Screen**
   - Scrollable form with validation
   - Country spinner selection
   - Secure password and PIN inputs

3. **Dashboard**
   - Prominent balance display card
   - Quick Actions grid (2x3 layout)
   - Recent transactions preview
   - Account settings section

4. **Transaction History**
   - Card-based transaction items
   - Color-coded transaction types
   - Date and amount display
   - Scrollable list

5. **Transaction Insights**
   - Interactive pie chart for categories
   - Bar chart for monthly trends
   - Total expense summary

## 🔒 Security Features

- **Password Encryption**: Secure password handling
- **Transaction PIN**: Additional authentication for transactions
- **Secure API Communication**: HTTPS support (configure in network security config)
- **Input Validation**: Client-side validation for all forms
- **Session Management**: Secure session handling

## 📱 Permissions

The app requires the following permissions:
- `INTERNET`: For API communication
- `CAMERA`: For QR code scanning (optional)
- `READ_EXTERNAL_STORAGE`: For file access (if needed)

## 🧪 Testing

### Unit Tests
- JUnit tests for business logic
- Validator tests for input validation

### Instrumented Tests
- AndroidX JUnit for UI tests
- Espresso for UI automation

### Manual Testing Checklist
- [ ] User registration
- [ ] User login
- [ ] Balance display
- [ ] Fund transfer
- [ ] Transaction history
- [ ] Profile view
- [ ] Password change
- [ ] TPIN change
- [ ] QR code generation/scanning
- [ ] Transaction insights
- [ ] EMI calculator

## 📊 Features in Detail

### Quick Actions Dashboard
The dashboard provides quick access to:
1. **Transactions**: View complete transaction history
2. **Fund Transfer**: Transfer money to other accounts
3. **QR Payment**: Generate/scan QR codes for payments
4. **Insights**: View spending analytics
5. **EMI Calculator**: Calculate loan EMIs

### Transaction Management
- Real-time transaction updates
- Transaction categorization
- Date-based filtering
- Amount and type display
- Description support

### Analytics & Insights
- Category-wise expense breakdown
- Monthly spending trends
- Total expense calculation
- Visual chart representations
- Interactive data exploration

## 🔮 Future Enhancements

- [ ] Biometric authentication (Fingerprint/Face ID)
- [ ] Push notifications for transactions
- [ ] Bill payment integration
- [ ] Investment tracking
- [ ] Budget management
- [ ] Export transaction history (PDF/CSV)
- [ ] Multi-language support
- [ ] Dark mode theme
- [ ] Transaction search and filters
- [ ] Recurring payments setup
- [ ] Account statements download
- [ ] Loan application integration

## 🐛 Troubleshooting

### Common Issues

1. **API Connection Failed**
   - Check backend server is running
   - Verify BASE_URL in ApiClient.java
   - Check network permissions in manifest
   - For emulator, ensure using `10.0.2.2` for localhost

2. **Build Errors**
   - Sync Gradle files: `File > Sync Project with Gradle Files`
   - Clean and rebuild: `Build > Clean Project`, then `Build > Rebuild Project`
   - Invalidate caches: `File > Invalidate Caches / Restart`

3. **QR Code Not Working**
   - Ensure camera permission is granted
   - Check if device has camera hardware
   - Verify ZXing library is properly included

4. **Charts Not Displaying**
   - Verify MPAndroidChart dependency
   - Check if data is being fetched correctly
   - Review TransactionInsightsActivity logs

## 📝 Additional Documentation

- `EMAIL_OTP_SETUP.md`: Email OTP setup instructions
- `OTP_SETUP_INSTRUCTIONS.md`: OTP configuration guide
- `TRANSACTION_INSIGHTS_SETUP.md`: Transaction insights setup
- SQL scripts for database setup

## 👥 Contributing

Contributions are welcome! Please follow these steps:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is part of an academic assignment.

## 👨‍💻 Author

Developed as part of the Android Programming course project.

## 🙏 Acknowledgments

- Material Design guidelines
- Retrofit library for API communication
- MPAndroidChart for data visualization
- ZXing for QR code functionality
- AndroidX libraries

---

**Note**: This is an educational project. For production use, additional security measures, encryption, and compliance with banking regulations would be required.

