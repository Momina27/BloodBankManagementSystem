# 🔴 Blood Bank Management System (BBMS)

### 👥 Student Information
* **Student Name:** Momina Touseef
* **Roll Number:** L1F23BSSE0330
* **Course:** Software Construction & Development (SCD)
* **Instructor:** Sir Abdul Rehman
* **University:** University of Central Punjab (UCP)

---

## 📝 Project Description
The **Blood Bank Management System** is a JavaFX-based desktop application designed to eliminate manual errors and streamline blood supply management in hospitals and blood banks. The system tracks available blood units in real-time, processes hospital requests, and features an automated Observer-based alert system for critical stock levels.

---

## ⚡ Core Features
* **Role-Based Authentication:** Secure access control for Admin and Staff sessions.
* **Donor Management:** Full CRUD operations to register, view, update, and delete donor profiles.
* **Real-Time Blood Inventory:** Dynamic visual card-style layout displaying stock metrics across all 8 blood groups.
* **Emergency Request Management:** Automates incoming hospital requests with options to approve/decline based on available units.
* **Observer-Driven Alert System:** Triggers live, prominent dashboard warnings when any blood group stock falls below a critical threshold (e.g., <= 2 units)..

---

## 🛠️ Design Patterns Used
* **Singleton Pattern:** Implemented in `SessionManager` for global login session tracking.
* **Factory Pattern:** Implemented in `BloodGroupFactory` to securely build blood unit objects.
* **Observer Pattern:** Implemented via `IAlertObserver` and `AlertService` for real-time critical inventory warnings.

---

## 🚀 How to Run
1. Open the project folder in **IntelliJ IDEA**.
2. Run `MainApplication.java` inside the `com.bloodbank.ui` package to start the system.
