import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authAPI } from '../services/api';

const Login = ({ onLogin }) => {
  const [formData, setFormData] = useState({
    loginId: '',
    password: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await authAPI.login(formData);
      const { token, customer } = response.data;
      
      // Transform the customer details to match expected format
      const customerData = {
        ...customer,
        customerId: customer.id  // Add customerId field
      };
      
      onLogin(token, customerData);
      navigate('/dashboard');
    } catch (error) {
      setError(
        error.response?.data?.message || 
        'Login failed. Please check your credentials.'
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="card" style={{ maxWidth: '400px', margin: '4rem auto' }}>
        <div className="text-center">
          <h1 style={{ color: '#667eea', marginBottom: '2rem' }}>
            🏦 Bank Management System
          </h1>
          <h2 style={{ marginBottom: '2rem', color: '#333' }}>Login</h2>
        </div>

        {error && (
          <div className="alert alert-error">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="loginId">Login ID</label>
            <input
              type="text"
              id="loginId"
              name="loginId"
              className="form-control"
              value={formData.loginId}
              onChange={handleChange}
              required
              placeholder="Enter your login ID"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              className="form-control"
              value={formData.password}
              onChange={handleChange}
              required
              placeholder="Enter your password"
            />
          </div>

          <button 
            type="submit" 
            className="btn" 
            style={{ width: '100%', marginBottom: '1rem' }}
            disabled={loading}
          >
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="text-center">
          <p>
            Don't have an account?{' '}
            <Link to="/register" style={{ color: '#667eea', fontWeight: 'bold' }}>
              Register here
            </Link>
          </p>
        </div>

        <div style={{ marginTop: '2rem', padding: '1rem', backgroundColor: '#f8f9fa', borderRadius: '8px' }}>
          <h4 style={{ color: '#667eea', marginBottom: '1rem' }}>Demo Credentials:</h4>
          <p><strong>Login ID:</strong> john_doe_001</p>
          <p><strong>Password:</strong> password123</p>
          <small style={{ color: '#6c757d' }}>
            Use these credentials to test the application
          </small>
        </div>
      </div>
    </div>
  );
};

export default Login;
