📘 Booking & Customer Microservices System
🧩 Overview
This project is a microservice‑based booking system built with Spring Boot, Docker, and a separate frontend.
The system consists of two independent services:

Customer‑service – handles customer registration, login, and customer administration.

Booking‑service – handles bookings, rooms, availability, and double‑booking prevention.

Each service has its own database, domain logic, and REST API.
The frontend communicates only with Customer‑service, which then communicates with Booking‑service.

🏗 Architecture
Customer‑service
Register customers

Login

Fetch customer data

Delete customers (only if they have no active bookings)

Validate customer IDs

Handle CORS and security

Act as a gateway to Booking‑service

Booking‑service
Create bookings

Update bookings

Delete bookings

Fetch bookings per customer

Fetch bookings per room

Check room availability

Prevent double bookings

Report whether a customer has active bookings (true/false)

Booking‑service contains no customer logic.
It only receives customerId as a number and links bookings to that ID.

🔌 Communication Between Services
Frontend → Customer‑service
All frontend API calls go to Customer‑service.

Customer‑service → Booking‑service
Customer‑service forwards booking‑related requests using a BookingClient.
Booking‑service responds with:

true → customer has active bookings

false → customer has no bookings

Customer‑service uses this to determine whether a customer can be safely deleted.

🧪 Testing With Thunder Client
Thunder Client is used to test the APIs directly.
CORS does not apply to Thunder Client because CORS only affects browsers.

Customer‑service (port 8081)
Register customer
Kod
POST http://localhost:8081/customers/register
Login
Kod
POST http://localhost:8081/customers/login
Delete customer
Kod
DELETE http://localhost:8081/customers/{id}
Booking‑service (port 8080)
Create booking
Kod
POST http://localhost:8080/bookings
Get bookings for customer
Kod
GET http://localhost:8080/bookings/customer/{id}
Check active bookings
Kod
GET http://localhost:8080/bookings/customer/{id}/active
Get available rooms
Kod
GET http://localhost:8080/bookings/available?date=2026-09-20
🔐 Security & CORS
Customer‑service allows requests only from the frontend origin:
http://localhost:63342

📂 Tech Stack
Java 17

Spring Boot

Spring Security

RestClient

Docker & Docker Compose

PostgreSQL

Thunder Client

HTML/CSS/JS frontend

🎯 Key Features
Fully separated microservices

Independent databases

Clear domain boundaries

Proper HTTP status codes

Double‑booking protection

Customer deletion only allowed when no bookings exist

Robust inter‑service communication

Easy API testing with Thunder Client

🚀 Future Improvements
JWT authentication

Admin dashboard

Email confirmations

Advanced room management

Centralized logging (ELK stack)
