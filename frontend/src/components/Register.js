import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authAPI } from '../services/api';

const Register = () => {
  const [formData, setFormData] = useState({
    name: '',
    loginId: '',
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '',
    address: '',
    state: '',
    country: '',
    panNumber: '',
    dob: '',
    accountNumber: '',
    accountType: 'SAVINGS'
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
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
    setSuccess('');

    // Validate passwords match
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    try {
      const { confirmPassword, ...registerData } = formData;
      await authAPI.register(registerData);
      
      setSuccess('Registration successful! Please login with your credentials.');
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (error) {
      setError(
        error.response?.data?.message || 
        'Registration failed. Please try again.'
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="card" style={{ maxWidth: '500px', margin: '2rem auto' }}>
        <div className="text-center">
          <h1 style={{ color: '#667eea', marginBottom: '1rem' }}>
            🏦 Bank Management System
          </h1>
          <h2 style={{ marginBottom: '2rem', color: '#333' }}>Register</h2>
        </div>

        {error && (
          <div className="alert alert-error">
            {error}
          </div>
        )}

        {success && (
          <div className="alert alert-success">
            {success}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name">Full Name</label>
            <input
              type="text"
              id="name"
              name="name"
              className="form-control"
              value={formData.name}
              onChange={handleChange}
              required
              placeholder="Enter your full name"
            />
          </div>

          <div className="grid grid-2" style={{ gap: '1rem' }}>
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
                placeholder="Create a unique login ID"
              />
            </div>

            <div className="form-group">
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                className="form-control"
                value={formData.email}
                onChange={handleChange}
                required
                placeholder="Enter email address"
              />
            </div>
          </div>

          <div className="grid grid-2" style={{ gap: '1rem' }}>
            <div className="form-group">
              <label htmlFor="phoneNumber">Phone Number</label>
              <input
                type="tel"
                id="phoneNumber"
                name="phoneNumber"
                className="form-control"
                value={formData.phoneNumber}
                onChange={handleChange}
                required
                placeholder="Enter phone number"
              />
            </div>

            <div className="form-group">
              <label htmlFor="dob">Date of Birth</label>
              <input
                type="date"
                id="dob"
                name="dob"
                className="form-control"
                value={formData.dob}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="address">Address</label>
            <textarea
              id="address"
              name="address"
              className="form-control"
              value={formData.address}
              onChange={handleChange}
              required
              placeholder="Enter full address"
              rows="2"
            />
          </div>

          <div className="grid grid-2" style={{ gap: '1rem' }}>
            <div className="form-group">
              <label htmlFor="state">State</label>
              <input
                type="text"
                id="state"
                name="state"
                className="form-control"
                value={formData.state}
                onChange={handleChange}
                required
                placeholder="Enter state"
              />
            </div>

            <div className="form-group">
              <label htmlFor="country">Country</label>
              <input
                type="text"
                id="country"
                name="country"
                className="form-control"
                value={formData.country}
                onChange={handleChange}
                required
                placeholder="Enter country"
              />
            </div>
          </div>

          <div className="grid grid-2" style={{ gap: '1rem' }}>
            <div className="form-group">
              <label htmlFor="panNumber">PAN Number</label>
              <input
                type="text"
                id="panNumber"
                name="panNumber"
                className="form-control"
                value={formData.panNumber}
                onChange={handleChange}
                required
                placeholder="Enter PAN number"
                pattern="[A-Z]{5}[0-9]{4}[A-Z]{1}"
                title="PAN format: ABCDE1234F"
              />
            </div>

            <div className="form-group">
              <label htmlFor="accountNumber">Account Number</label>
              <input
                type="text"
                id="accountNumber"
                name="accountNumber"
                className="form-control"
                value={formData.accountNumber}
                onChange={handleChange}
                required
                placeholder="Enter account number"
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="accountType">Account Type</label>
            <select
              id="accountType"
              name="accountType"
              className="form-control"
              value={formData.accountType}
              onChange={handleChange}
              required
            >
              <option value="SAVINGS">Savings Account</option>
              <option value="CURRENT">Current Account</option>
              <option value="FIXED_DEPOSIT">Fixed Deposit Account</option>
              <option value="RECURRING_DEPOSIT">Recurring Deposit Account</option>
            </select>
          </div>

          <div className="grid grid-2" style={{ gap: '1rem' }}>
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
                placeholder="Enter password"
                minLength="6"
              />
            </div>

            <div className="form-group">
              <label htmlFor="confirmPassword">Confirm Password</label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                className="form-control"
                value={formData.confirmPassword}
                onChange={handleChange}
                required
                placeholder="Confirm password"
                minLength="6"
              />
            </div>
          </div>

          <button 
            type="submit" 
            className="btn" 
            style={{ width: '100%', marginBottom: '1rem' }}
            disabled={loading}
          >
            {loading ? 'Registering...' : 'Register'}
          </button>
        </form>

        <div className="text-center">
          <p>
            Already have an account?{' '}
            <Link to="/login" style={{ color: '#667eea', fontWeight: 'bold' }}>
              Login here
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;
