# Booking Troubleshooting Guide

## Common Issues and Solutions

### 1. "Booking failed. Please check console for details."

**Check these things in order:**

#### A. Is the user logged in?
- Open browser DevTools (F12)
- Go to Application/Storage tab
- Check if there's a `token` in localStorage
- If NO token: You must login first at http://localhost:3000/login

#### B. Is the backend running?
- Check terminal where backend is running
- Should see: "Started CarRentalSystemApplication"
- If not running: `cd Backend; .\mvnw.cmd spring-boot:run`

#### C. Check Browser Console (F12 > Console)
Look for these messages:

**401 Unauthorized:**
```
Error: Request failed with status code 401
```
**Solution:** You're not logged in. Go to /login

**404 Not Found:**
```
Error: Request failed with status code 404
```
**Solution:** Car not found or wrong car ID

**Network Error:**
```
Network error. Please check your connection...
```
**Solution:** Backend is not running on port 8080

**CORS Error:**
```
Access to XMLHttpRequest at 'http://localhost:8080/api/bookings' from origin 'http://localhost:3000' has been blocked by CORS policy
```
**Solution:** Backend CORS configuration issue (should already be fixed)

#### D. Check Backend Console
Look for these in the backend terminal:

**Success:**
```
=== Booking Request Received ===
Booking data: ...
Current user: testuser
Booking saved successfully: 1
```

**Error:**
```
Error creating booking: ...
```

### 2. Testing Steps

1. **Login First:**
   ```
   Go to: http://localhost:3000/login
   Username: admin (or your username)
   Password: admin123 (or your password)
   ```

2. **Go to Browse Page:**
   ```
   http://localhost:3000/cars
   ```

3. **Click on a car** to go to booking page

4. **Fill the form:**
   - Pick-up Location: Enter any location
   - Pick-up Date: Select today or future date
   - Pick-up Time: Select any time
   - Passengers: 1-5

5. **Click "Reserve Now"**

6. **Check Console** (F12) for any errors

### 3. Manual API Test

Test the booking endpoint directly:

```bash
# 1. Login first to get token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Copy the token from response

# 2. Create booking
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "car": {"id": 1},
    "pickupLocation": "Test Location",
    "pickupDate": "2025-10-20",
    "pickupTime": "10:00",
    "passengers": 2,
    "totalPrice": 180.00,
    "status": "PENDING"
  }'
```

### 4. Check Database

If booking appears to succeed but you don't see it:

```sql
-- Check if booking was saved
SELECT * FROM booking ORDER BY id DESC LIMIT 5;

-- Check car details
SELECT * FROM car WHERE id = 1;

-- Check user details
SELECT * FROM users WHERE username = 'admin';
```

### 5. Common Fixes

**Clear browser cache:**
```
Ctrl + Shift + Delete
Select "Cached images and files"
Click "Clear data"
```

**Restart both servers:**
```
# Kill backend (Ctrl+C in backend terminal)
# Kill frontend (Ctrl+C in frontend terminal)

# Start backend
cd Backend
.\mvnw.cmd spring-boot:run

# Start frontend (in new terminal)
cd car-rental-frontend
npm start
```

### 6. Expected Console Output

**Frontend Console (Success):**
```
API Request: {method: 'POST', url: '/bookings', ...}
Sending booking data: {car: {id: 1}, pickupLocation: 'Test', ...}
API Response: {status: 200, data: {id: 1, ...}}
Booking successful! Redirecting to your profile...
```

**Backend Console (Success):**
```
=== Booking Request Received ===
Booking data: Booking(id=null, car=Car(id=1,...), ...)
Authentication: UsernamePasswordAuthenticationToken...
Auth name: admin
Current user: admin
Hibernate: insert into booking (...) values (...)
Booking saved successfully: 1
```

## Still Not Working?

1. Check the exact error message in console
2. Copy the full error stack trace
3. Check backend logs for detailed error
4. Verify database connection is working
5. Ensure car with ID exists in database
