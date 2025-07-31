import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { customerAPI, loanAPI } from '../services/api';

const Dashboard = ({ user }) => {
  const [accountData, setAccountData] = useState(null);
  const [loans, setLoans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        
        // Fetch account details and loans in parallel
        const [accountResponse, loansResponse] = await Promise.all([
          customerAPI.getAccount(user.customerId),
          loanAPI.getAll(user.customerId)
        ]);

        setAccountData(accountResponse.data.account || accountResponse.data);
        setLoans(loansResponse.data || []);
      } catch (error) {
        setError('Failed to load dashboard data');
        console.error('Dashboard error:', error);
      } finally {
        setLoading(false);
      }
    };

    if (user?.customerId) {
      fetchDashboardData();
    } else {
      setError('No user ID found. Please login again.');
      setLoading(false);
    }
  }, [user]);

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount || 0);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString();
  };

  const getStatusColor = (status) => {
    switch (status?.toLowerCase()) {
      case 'approved': return 'text-success';
      case 'pending': return 'text-warning';
      case 'rejected': return 'text-danger';
      default: return '';
    }
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="text-center" style={{ marginBottom: '2rem' }}>
        <h1 style={{ color: '#667eea' }}>
          Welcome to Your Dashboard, {user.firstName}!
        </h1>
        <p style={{ color: '#666', fontSize: '1.1rem' }}>
          Manage your banking needs from one place
        </p>
      </div>

      {error && (
        <div className="alert alert-error">
          {error}
        </div>
      )}

      {/* Account Overview */}
      <div className="grid grid-3">
        <div className="card">
          <h3 style={{ color: '#667eea', marginBottom: '1rem' }}>
            💰 Account Balance
          </h3>
          <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#28a745' }}>
            {formatCurrency(accountData?.balance)}
          </div>
          <p style={{ color: '#666', marginTop: '0.5rem' }}>
            Account Type: {accountData?.accountType || 'N/A'}
          </p>
          <p style={{ color: '#666' }}>
            Account Number: {accountData?.accountNumber || 'N/A'}
          </p>
        </div>

        <div className="card">
          <h3 style={{ color: '#667eea', marginBottom: '1rem' }}>
            📊 Total Loans
          </h3>
          <div style={{ fontSize: '2rem', fontWeight: 'bold', color: '#667eea' }}>
            {loans.length}
          </div>
          <p style={{ color: '#666', marginTop: '0.5rem' }}>
            Active Loans
          </p>
          <Link to="/apply-loan" className="btn btn-secondary" style={{ marginTop: '1rem' }}>
            Apply for New Loan
          </Link>
        </div>

        <div className="card">
          <h3 style={{ color: '#667eea', marginBottom: '1rem' }}>
            🏦 Quick Actions
          </h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
            <Link to="/account" className="btn">
              View Account Details
            </Link>
            <Link to="/apply-loan" className="btn btn-secondary">
              Apply for Loan
            </Link>
          </div>
        </div>
      </div>

      {/* Recent Loans */}
      <div className="card">
        <h3 style={{ color: '#667eea', marginBottom: '1.5rem' }}>
          📋 Your Loans
        </h3>
        
        {loans.length === 0 ? (
          <div className="text-center" style={{ padding: '2rem' }}>
            <p style={{ color: '#666', fontSize: '1.1rem' }}>
              You don't have any loans yet.
            </p>
            <Link to="/apply-loan" className="btn" style={{ marginTop: '1rem' }}>
              Apply for Your First Loan
            </Link>
          </div>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table className="table">
              <thead>
                <tr>
                  <th>Loan Type</th>
                  <th>Amount</th>
                  <th>Interest Rate</th>
                  <th>EMI</th>
                  <th>Status</th>
                  <th>Application Date</th>
                </tr>
              </thead>
              <tbody>
                {loans.slice(0, 5).map((loan) => (
                  <tr key={loan.loanId}>
                    <td>{loan.loanType}</td>
                    <td>{formatCurrency(loan.amount)}</td>
                    <td>{loan.interestRate}%</td>
                    <td>{formatCurrency(loan.emi)}</td>
                    <td>
                      <span className={getStatusColor(loan.status)}>
                        {loan.status}
                      </span>
                    </td>
                    <td>{formatDate(loan.applicationDate)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            
            {loans.length > 5 && (
              <div className="text-center" style={{ marginTop: '1rem' }}>
                <Link to="/account" className="btn btn-secondary">
                  View All Loans
                </Link>
              </div>
            )}
          </div>
        )}
      </div>

      {/* Customer Information */}
      <div className="card">
        <h3 style={{ color: '#667eea', marginBottom: '1.5rem' }}>
          👤 Your Information
        </h3>
        <div className="grid grid-2">
          <div>
            <p><strong>Name:</strong> {user.firstName} {user.lastName}</p>
            <p><strong>Email:</strong> {user.email}</p>
            <p><strong>Phone:</strong> {user.phoneNumber}</p>
          </div>
          <div>
            <p><strong>Address:</strong> {user.address}</p>
            <p><strong>Customer ID:</strong> {user.customerId}</p>
            <p><strong>Member Since:</strong> {formatDate(user.createdDate)}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
