import React, { useState, useEffect } from 'react';
import { customerAPI, loanAPI } from '../services/api';

const AccountDetails = ({ user }) => {
  const [accountData, setAccountData] = useState(null);
  const [loans, setLoans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('account');

  useEffect(() => {
    const fetchAccountData = async () => {
      try {
        setLoading(true);
        
        const [accountResponse, loansResponse] = await Promise.all([
          customerAPI.getAccount(user.customerId),
          loanAPI.getAll(user.customerId)
        ]);

        setAccountData(accountResponse.data.account || accountResponse.data);
        setLoans(loansResponse.data || []);
      } catch (error) {
        setError('Failed to load account data');
        console.error('Account error:', error);
      } finally {
        setLoading(false);
      }
    };

    if (user?.customerId) {
      fetchAccountData();
    }
  }, [user]);

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount || 0);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const getStatusColor = (status) => {
    switch (status?.toLowerCase()) {
      case 'approved': return 'text-success';
      case 'pending': return 'text-warning';
      case 'rejected': return 'text-danger';
      default: return '';
    }
  };

  const getStatusBadge = (status) => {
    const color = getStatusColor(status);
    return (
      <span className={`${color}`} style={{ 
        padding: '4px 8px', 
        borderRadius: '4px',
        backgroundColor: color.includes('success') ? '#d4edda' : 
                        color.includes('warning') ? '#fff3cd' : 
                        color.includes('danger') ? '#f8d7da' : '#e9ecef',
        fontWeight: 'bold',
        fontSize: '0.875rem'
      }}>
        {status}
      </span>
    );
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
        <h1 style={{ color: '#667eea' }}>Account Details</h1>
        <p style={{ color: '#666' }}>Comprehensive view of your banking information</p>
      </div>

      {error && (
        <div className="alert alert-error">
          {error}
        </div>
      )}

      {/* Tab Navigation */}
      <div style={{ 
        display: 'flex', 
        gap: '1rem', 
        marginBottom: '2rem',
        borderBottom: '2px solid #e1e5e9',
        paddingBottom: '1rem'
      }}>
        <button
          className={`btn ${activeTab === 'account' ? '' : 'btn-secondary'}`}
          onClick={() => setActiveTab('account')}
        >
          🏦 Account Info
        </button>
        <button
          className={`btn ${activeTab === 'loans' ? '' : 'btn-secondary'}`}
          onClick={() => setActiveTab('loans')}
        >
          💰 Loans ({loans.length})
        </button>
        <button
          className={`btn ${activeTab === 'profile' ? '' : 'btn-secondary'}`}
          onClick={() => setActiveTab('profile')}
        >
          👤 Profile
        </button>
      </div>

      {/* Account Information Tab */}
      {activeTab === 'account' && (
        <div className="card">
          <h3 style={{ color: '#667eea', marginBottom: '2rem' }}>
            🏦 Account Information
          </h3>
          
          {accountData ? (
            <div className="grid grid-2">
              <div>
                <div style={{ marginBottom: '1.5rem' }}>
                  <h4 style={{ color: '#333', marginBottom: '1rem' }}>Account Details</h4>
                  <p><strong>Account Number:</strong> {accountData.accountNumber}</p>
                  <p><strong>Account Type:</strong> {accountData.accountType}</p>
                  <p><strong>Current Balance:</strong> 
                    <span style={{ 
                      fontSize: '1.25rem', 
                      fontWeight: 'bold', 
                      color: '#28a745',
                      marginLeft: '0.5rem'
                    }}>
                      {formatCurrency(accountData.balance)}
                    </span>
                  </p>
                  <p><strong>Account Status:</strong> 
                    <span style={{ color: '#28a745', marginLeft: '0.5rem' }}>Active</span>
                  </p>
                </div>
              </div>
              
              <div>
                <div style={{ marginBottom: '1.5rem' }}>
                  <h4 style={{ color: '#333', marginBottom: '1rem' }}>Account Summary</h4>
                  <div style={{ 
                    backgroundColor: '#f8f9fa', 
                    padding: '1rem', 
                    borderRadius: '8px' 
                  }}>
                    <p><strong>Total Loans:</strong> {loans.length}</p>
                    <p><strong>Active Loans:</strong> {loans.filter(loan => loan.status === 'APPROVED').length}</p>
                    <p><strong>Pending Applications:</strong> {loans.filter(loan => loan.status === 'PENDING').length}</p>
                    <p><strong>Total Loan Amount:</strong> {formatCurrency(
                      loans.filter(loan => loan.status === 'APPROVED')
                           .reduce((sum, loan) => sum + loan.amount, 0)
                    )}</p>
                  </div>
                </div>
              </div>
            </div>
          ) : (
            <p>No account information available.</p>
          )}
        </div>
      )}

      {/* Loans Tab */}
      {activeTab === 'loans' && (
        <div className="card">
          <h3 style={{ color: '#667eea', marginBottom: '2rem' }}>
            💰 Your Loans
          </h3>
          
          {loans.length === 0 ? (
            <div className="text-center" style={{ padding: '3rem' }}>
              <p style={{ fontSize: '1.1rem', color: '#666', marginBottom: '1rem' }}>
                No loans found
              </p>
              <p style={{ color: '#999' }}>
                You haven't applied for any loans yet.
              </p>
            </div>
          ) : (
            <div style={{ overflowX: 'auto' }}>
              <table className="table">
                <thead>
                  <tr>
                    <th>Loan ID</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Interest Rate</th>
                    <th>Term</th>
                    <th>EMI</th>
                    <th>Status</th>
                    <th>Application Date</th>
                  </tr>
                </thead>
                <tbody>
                  {loans.map((loan) => (
                    <tr key={loan.loanId}>
                      <td>#{loan.loanId}</td>
                      <td>{loan.loanType}</td>
                      <td>{formatCurrency(loan.amount)}</td>
                      <td>{loan.interestRate}%</td>
                      <td>{loan.termMonths} months</td>
                      <td>{formatCurrency(loan.emi)}</td>
                      <td>{getStatusBadge(loan.status)}</td>
                      <td>{formatDate(loan.applicationDate)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {/* Profile Tab */}
      {activeTab === 'profile' && (
        <div className="card">
          <h3 style={{ color: '#667eea', marginBottom: '2rem' }}>
            👤 Profile Information
          </h3>
          
          <div className="grid grid-2">
            <div>
              <h4 style={{ color: '#333', marginBottom: '1rem' }}>Personal Information</h4>
              <p><strong>First Name:</strong> {user.firstName}</p>
              <p><strong>Last Name:</strong> {user.lastName}</p>
              <p><strong>Email:</strong> {user.email}</p>
              <p><strong>Phone Number:</strong> {user.phoneNumber}</p>
              <p><strong>Customer ID:</strong> {user.customerId}</p>
            </div>
            
            <div>
              <h4 style={{ color: '#333', marginBottom: '1rem' }}>Additional Details</h4>
              <p><strong>Address:</strong> {user.address}</p>
              <p><strong>Member Since:</strong> {formatDate(user.createdDate)}</p>
              <p><strong>Account Status:</strong> 
                <span style={{ color: '#28a745', marginLeft: '0.5rem' }}>Active</span>
              </p>
            </div>
          </div>
          
          <div style={{ 
            marginTop: '2rem', 
            padding: '1rem', 
            backgroundColor: '#f8f9fa', 
            borderRadius: '8px' 
          }}>
            <h5 style={{ color: '#667eea', marginBottom: '1rem' }}>
              🛡️ Security Information
            </h5>
            <p><strong>Last Login:</strong> Today</p>
            <p><strong>Account Security:</strong> 
              <span style={{ color: '#28a745', marginLeft: '0.5rem' }}>Secure</span>
            </p>
            <p><strong>Two-Factor Authentication:</strong> 
              <span style={{ color: '#ffc107', marginLeft: '0.5rem' }}>Recommended</span>
            </p>
          </div>
        </div>
      )}
    </div>
  );
};

export default AccountDetails;
