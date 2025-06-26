# OPSC6311-Part3
ST10366390

Readme

 Nomisma - Personal Budgeting App

Nomisma is a secure personal budgeting application for Android that helps users track expenses with enhanced security features.

Features
User Authentication
Secure login and signup functionality

BCrypt password hashing for user data protection

Salted hashes protect against rainbow table attacks

Expense Management
Add new expenses with name and amount

View and track all expenses

Simple interface for quick expense entry

Category System
Create custom categories for expenses

Organize expenses by category

View all categories in a clean list

Data Persistence
SQLite database for local storage

All data persists between app sessions

Parameterized queries prevent SQL injection

Technical Overview
Architecture
Activities:

LoginActivity: Handles secure user authentication

SignupActivity: Manages new user registration with password hashing

MainActivity: Main dashboard (entry point after login)

CategoriesActivity: Displays and manages expense categories

NewCategoryActivity: Interface for adding new categories

ExpenseEntry: Screen for entering new expenses

Database:

DatabaseHelper class extends SQLiteOpenHelper

Three main tables:

users: Stores securely hashed user credentials

expenses: Tracks all expense entries

categories: Maintains expense categories

Security Implementation
kotlin
// Updated DatabaseHelper.kt with password hashing
import org.mindrot.jbcrypt.BCrypt

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(...) {
    
    // Modified insertUser function with hashing
    fun insertUser(username: String, password: String): Long {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val value = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, hashedPassword)
        }
        val db = writableDatabase
        return db.insert(TABLE_USERS, null, value)
    }

    // Modified readDatabase function with hash verification
    fun readDatabase(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val storedHash = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            cursor.close()
            return BCrypt.checkpw(password, storedHash)
        }
        cursor.close()
        return false
    }
}
Key Components
Data binding for efficient UI updates

RecyclerView for displaying lists (categories)

Toast messages for user feedback

Intent system for navigation between activities

BCrypt for secure password hashing

Installation
Clone this repository

Add the BCrypt dependency to your app-level build.gradle:

gradle
implementation 'org.mindrot:jbcrypt:0.4'
Open in Android Studio

Build and run on an emulator or physical device

Usage
First-time users:

Create an account via Signup screen

Set up your initial expense categories

Regular use:

Log in with your credentials

Add expenses as they occur

Review your spending by category

Security Best Practices
Never store plaintext passwords

Uses industry-standard bcrypt hashing algorithm

Automatic salt generation with each password

Adaptive hashing makes brute-force attempts computationally expensive

Plaintext passwords never persist in memory

Future Enhancements
Budget tracking and alerts

Expense reports and visualizations

Cloud sync for multi-device access

Export data to CSV/Excel

Biometric authentication

Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

License
MIT

Login Flow Diagram:

sequenceDiagram
    User->>LoginActivity: Enters credentials
    LoginActivity->>DatabaseHelper: Requests authentication
    DatabaseHelper->>Database: Retrieves stored hash
    Database-->>DatabaseHelper: Returns hash
    DatabaseHelper->>BCrypt: Verifies password
    BCrypt-->>DatabaseHelper: Returns verification result
    DatabaseHelper-->>LoginActivity: Returns auth status
    LoginActivity->>User: Grants/Denies access
