package com.learning;

import com.learning.dao.CustomerDao;
import com.learning.dao.TransactionDao;
import com.learning.model.*;

import java.time.Instant;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerDao customerDao = new CustomerDao();
        TransactionDao transactionDao = new TransactionDao();

        while (true) {
            System.out.println("1. New Registration");
            System.out.println("2. Existing User, Login!");
            System.out.println("3. Delete Account");
            System.out.println("Enter your option");
            int option = scanner.nextInt();
            scanner.nextLine();
            outerLoop:
            switch (option) {
                case 1:
                    System.out.println("Enter Your Details");
                    System.out.println("Enter full name: ");
                    String fullName = scanner.nextLine();

                    System.out.println("Enter Address: ");
                    String address = scanner.nextLine();

                    System.out.println("Enter Aadhar Number: ");
                    String aadharNumber = scanner.nextLine();

                    boolean genderRef = true;
                    GenderType gender = null;
                    do {
                        System.out.println("Select Gender: ");
                        System.out.println("1. Male");
                        System.out.println("2. Female");
                        System.out.println("3. Others");
                        int genderInput = scanner.nextInt();
                        if (genderInput != 1 && genderInput != 2 && genderInput != 3) {
                            System.out.println("Invalid Gender, select out of given 3 options");
                        } else {
                            if (genderInput == 1) {
                                gender = GenderType.MALE;
                            } else if (genderInput == 2) {
                                gender = GenderType.FEMALE;
                            } else {
                                gender = GenderType.OTHER;
                            }
                            genderRef = false;
                        }
                    } while (genderRef);
                    scanner.nextLine();

                    boolean accountTypeRef = true;
                    AccountType accountType = null;
                    do {
                        System.out.println("Select Account Type: ");
                        System.out.println("1. Savings");
                        System.out.println("2. Current");
                        int accountTypeInput = scanner.nextInt();
                        if (accountTypeInput != 1 && accountTypeInput != 2) {
                            System.out.println("Invalid Account Type, select out of given 2 options");
                        } else {
                            accountType = accountTypeInput == 1 ? AccountType.SAVINGS : AccountType.CURRENT;
                            accountTypeRef = false;
                        }
                    } while (accountTypeRef);
                    scanner.nextLine();

                    System.out.println("Enter balance: ");
                    double balance = scanner.nextDouble();

                    System.out.println("Enter Phone Number: ");
                    String phoneNumber = scanner.next();

                    System.out.println("Enter Username: ");
                    String username = scanner.next();

                    System.out.println("Enter Password: ");
                    String password = scanner.next();

                    Customer customer = new Customer(UUID.randomUUID(), username, fullName, String.valueOf(customerDao.count() + 1), address, aadharNumber, gender, accountType, balance, phoneNumber, password);
                    if (customerDao.insertCustomer(customer)) {
                        System.out.println("Registration Successful with username: " + username);
                    } else {
                        System.out.println("Registration Failed");
                    }
                    break;
                case 2:
                    System.out.println("Login..");
                    System.out.println("Enter Username: ");
                    boolean userInputRef = true;
                    Customer existingCustomer;
                    do {
                        String usernameInput = scanner.nextLine();
                        existingCustomer = customerDao.getByUsername(usernameInput.trim());
                        if (existingCustomer == null) {
                            System.out.println("Invalid Username");
                            System.out.println("Enter Valid Username: ");
                        } else {
                            userInputRef = false;
                        }
                    } while (userInputRef);

                    System.out.println("Enter Password: ");
                    boolean passwordInputRef = true;
                    do {
                        String passwordInput = scanner.nextLine();
                        if (!existingCustomer.getPassword().equals(passwordInput)) {
                            System.out.println("Invalid Password");
                            System.out.println("Enter Valid Password: ");
                        } else {
                            passwordInputRef = false;
                        }
                    } while (passwordInputRef);

                    System.out.println();
                    System.out.println();

                    System.out.println("Login Successful with username: " + existingCustomer.getUsername());

                    System.out.println();
                    System.out.println();

                    System.out.println("Enter option to proceed..");
                    System.out.println("1. View Details");
                    System.out.println("2. Deposit");
                    System.out.println("3. Withdraw");
                    System.out.println("4. View all transactions");
                    System.out.println("5. Logout");

                    int menuOption = scanner.nextInt();
                    switch (menuOption) {
                        case 1:
                            System.out.println("Full Name: " + existingCustomer.getFullName());
                            System.out.println("Address: " + existingCustomer.getAddress());
                            System.out.println("Aadhar Number: " + existingCustomer.getAadharNumber());
                            System.out.println("Gender: " + existingCustomer.getGender());
                            System.out.println("Account Type: " + existingCustomer.getAccountType());
                            System.out.println("Balance: " + existingCustomer.getBalance());
                            System.out.println("Phone Number: " + existingCustomer.getPhoneNumber());
                            break;
                        case 2:
                            System.out.println("Enter deposit amount: ");
                            double depositAmount = scanner.nextDouble();
                            existingCustomer.setBalance(existingCustomer.getBalance() + depositAmount);
                            Transaction depositTransaction = new Transaction(UUID.randomUUID(), existingCustomer.getUuid(), TransactionType.DEPOSIT, depositAmount, Instant.now());
                            if (customerDao.updateCustomer(existingCustomer) && transactionDao.insert(depositTransaction)) {
                                System.out.println("Transaction Successful");
                            }
                            System.out.println("Deposit Successful, updated balance: " + existingCustomer.getBalance());
                            break;
                        case 3:
                            System.out.println("Enter Withdraw amount: ");
                            double withdrawAmount = scanner.nextDouble();
                            if (withdrawAmount <= existingCustomer.getBalance()) {
                                existingCustomer.setBalance(existingCustomer.getBalance() - withdrawAmount);
                                Transaction withdrawTransaction = new Transaction(UUID.randomUUID(), existingCustomer.getUuid(), TransactionType.WITHDRAW, withdrawAmount, Instant.now());
                                if (customerDao.updateCustomer(existingCustomer) && transactionDao.insert(withdrawTransaction)) {
                                    System.out.println("Transaction Successful");
                                }
                                System.out.println("Withdraw Successful, updated balance: " + existingCustomer.getBalance());
                            } else {
                                System.out.println("Insufficient balance");
                            }
                            break;

                        case 4:
                            List<Transaction> transactions = transactionDao.getAllByCustomerId(existingCustomer.getUuid());
                            System.out.println("Transactions:");
                            System.out.print("Id " + "\t\t\t\t\t\t\t\t\t\t");
                            System.out.print("Type " + "\t\t");
                            System.out.print("Amount " + "\t");
                            System.out.print("Time " + "\t");
                            System.out.println();
                            for (Transaction transaction : transactions) {
                                System.out.print(transaction.getUuid() + "\t");
                                System.out.print(transaction.getTransactionType() + "\t\t");
                                System.out.print(transaction.getTransactionAmount() + "\t");
                                System.out.print(transaction.getTransactionDate() + "\t");
                                System.out.println();
                            }
                            break;

                        case 5:
                            System.out.println("Logout Successful");
                            System.exit(0);

                        default:
                            break;
                    }
            }
        }
    }
}
