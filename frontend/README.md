# Bank Management System - Frontend

A modern React-based frontend application for the Bank Management System.

## Features

### 🔐 Authentication
- User login and registration
- JWT token-based authentication
- Secure route protection

### 🏦 Banking Features
- Account overview dashboard
- Real-time account balance display
- Custom interest rate loan applications
- Loan management and tracking
- EMI calculator with real-time calculations

### 📱 User Interface
- Responsive design for all devices
- Modern gradient-based styling
- Intuitive navigation with tab system
- Real-time form validation
- Loading states and error handling

### 💰 Loan Management
- Apply for different loan types (Personal, Home, Auto, Education)
- Custom interest rate selection (1% - 50%)
- Real-time EMI calculation
- Loan status tracking
- Comprehensive loan history

## Technology Stack

- **React 18** - Frontend framework
- **React Router 6** - Client-side routing
- **Axios** - HTTP client for API calls
- **Modern CSS** - Gradient-based responsive design
- **JWT Authentication** - Secure token-based auth

## API Integration

The frontend integrates with the Spring Boot backend API:
- Base URL: `http://localhost:8080/api/v1/bms`
- Authentication: Bearer token in headers
- Automatic token refresh handling

## Pages and Components

### Authentication Pages
- **Login** - User authentication with demo credentials
- **Register** - New user registration with account type selection

### Main Application Pages
- **Dashboard** - Overview with account balance, loans summary, and quick actions
- **Account Details** - Comprehensive account information with tabbed interface
- **Loan Application** - Apply for loans with custom interest rates and EMI calculator

### Shared Components
- **Navbar** - Navigation with user info and logout
- **API Service** - Centralized API calls with interceptors

## Getting Started

### Prerequisites
- Node.js 16+ installed
- Backend Spring Boot application running on port 8080

### Installation

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. Open your browser and visit:
   ```
   http://localhost:3000
   ```

### Demo Credentials

For testing the application, use these demo credentials:
- **Email:** john.doe@example.com
- **Password:** password123

## Development

### Available Scripts

- `npm start` - Start development server
- `npm build` - Build for production
- `npm test` - Run tests
- `npm eject` - Eject from Create React App

### Project Structure

```
frontend/
├── public/
│   ├── index.html
│   └── manifest.json
├── src/
│   ├── components/
│   │   ├── Login.js
│   │   ├── Register.js
│   │   ├── Dashboard.js
│   │   ├── Navbar.js
│   │   ├── LoanApplication.js
│   │   └── AccountDetails.js
│   ├── services/
│   │   └── api.js
│   ├── App.js
│   ├── index.js
│   └── index.css
└── package.json
```

## Features Highlight

### Custom Interest Rate Loans
- Users can set their own interest rates between 1% and 50%
- Real-time EMI calculation shows monthly payment, total amount, and total interest
- Support for multiple loan types and terms

### Responsive Design
- Mobile-first approach
- Gradient backgrounds for modern look
- Card-based layout for better content organization
- Smooth animations and hover effects

### Security
- JWT token management
- Automatic logout on token expiration
- Secure API calls with authorization headers
- Protected routes for authenticated users only

## Integration with Backend

The frontend is designed to work seamlessly with the Spring Boot backend:
- All API endpoints are properly mapped
- Custom interest rate feature fully integrated
- Real-time EMI calculations
- Comprehensive error handling

## Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is part of the Bank Management System and follows the same licensing terms.
