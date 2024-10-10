# Money Monitor - Expense Tracker

**Money Monitor** is a feature-rich Android application for tracking and managing your personal finances. The app helps you monitor your **income**, **expenses**, and **transfers** effortlessly, providing insights into your spending habits through visually appealing charts.

## Features

- **Track Income, Expenses, and Transfers**: 
  Easily add, view, and manage all your financial records in one place.
  
- **Categorization**:
  Organize your transactions by categories like *groceries*, *rent*, *salary*, etc.
  
- **Payment Methods**:
  Record payments made through various methods (e.g., cash, credit card, bank transfer).
  
- **Charts & Analysis**:
  Get visual insights into your spending using **pie charts** that display your expenses by category.

- **Monthly and Yearly Analysis**:
  Switch between time periods to get detailed financial reports for each month and year.

- **Profile Management**:
  Update your personal profile, including your name, phone number, and more.

- **PIN Protection**:
  Secure your data with a PIN to protect sensitive financial information.

## Screenshots

<!-- First row with three images -->
<div style="display: flex; justify-content: space-between;">
  <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20213216.png" alt="Expense Overview" width="200"/>
  <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20214318.png" alt="Monthly Report" width="200"/>
  <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20213758.png" alt="Transfer History" width="200"/>
    <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20214451.png" alt="Notifications" width="200"/>
</div>

<details>
  <summary>Show All Images</summary>

  <!-- The rest of the images are hidden inside this collapsible section -->
  
  <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20214409.png" alt="Detailed Expense Breakdown" width="200"/>
    <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20214335.png" alt="Settings" width="200"/>
  <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20214510.png" alt="User Profile" width="200"/>
  <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20214537.png" alt="Budget Planner" width="200"/>
  <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20214556.png" alt="App Feedback" width="200"/>
  <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20213741.png" alt="Income Tracker" width="200"/>
  <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20213116.png" alt="Home Screen" width="200"/>
  <img src="https://raw.githubusercontent.com/Bhadanitirth/Money-Monito/main/Photo/Screenshot%202024-10-10%20214924.png" alt="Help and Support" width="200"/>

</details>




## Installation

### Prerequisites
- **Android Studio** installed on your machine.
- **Java Development Kit (JDK)** 8 or higher.
- **Gradle** version compatible with the project.

### Steps
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/money-monitor-expense-tracker.git
   ```

2. **Open the project in Android Studio**:
   - Navigate to `File > Open` and select the cloned directory.

3. **Build and run the project**:
   - Ensure your Android device/emulator is connected and click `Run` in Android Studio.

## Usage

1. **Add Transactions**:
   - Record income, expenses, or transfers by navigating to the respective section. Fill in details such as amount, category, date, and payment method.

2. **Visualize Spending**:
   - View your expenses in categorized pie charts in the **Analysis** section.

3. **Update Profile**:
   - Go to the **Profile** section to update your personal details like phone number, name, and date of birth.

4. **Security**:
   - Enable PIN protection to secure your data.

## Code Structure

```plaintext
├── app/
│   ├── java/com/moneymonitor
│   │   ├── activities/           # All activities like Home, AddTransaction, etc.
│   │   ├── adapters/             # Custom adapters for RecyclerViews
│   │   ├── database/             # SQLite Database helper classes
│   │   ├── fragments/            # Fragments for different views (Income, Expense, Transfer)
│   │   ├── models/               # Data models for transactions, categories, etc.
│   └── res/
│       ├── layout/               # XML layout files
│       ├── drawable/             # Icons and images used in the app
│       ├── values/               # Strings, colors, and themes
└── build.gradle                  # Gradle dependencies and project settings
```

## Technologies Used

- **Java**: The app is built using Java for Android development.
- **SQLite**: Local database to store transactions and user data.
- **MPAndroidChart**: Used for creating pie charts and other visualizations.

## Contributing

We welcome contributions to improve **Money Monitor**! Follow these steps:

1. **Fork the repository**.
2. Create a new branch for your feature or bug fix:
   ```bash
   git checkout -b feature-branch
   ```

3. Commit your changes:
   ```bash
   git commit -m "Add a feature"
   ```

4. Push the branch:
   ```bash
   git push origin feature-branch
   ```

5. Submit a pull request.

## Contact

For any questions or feedback, please reach out:

- **Name**: Bhadani Tirth
- **Email**: bhadanitirth@gmail.com

- **Name**: Agrawal Gaurang
- **Email**: agrawalgaurang5425@gmail.com
