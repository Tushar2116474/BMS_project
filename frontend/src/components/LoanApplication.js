import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loanAPI } from '../services/api';

const LoanApplication = ({ user }) => {
  const [formData, setFormData] = useState({
    loanType: 'PERSONAL',
    amount: '',
    interestRate: '',
    termMonths: '12'
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [emiCalculation, setEmiCalculation] = useState(null);
  const navigate = useNavigate();

  const loanTypes = [
    { value: 'PERSONAL', label: 'Personal Loan' },
    { value: 'HOME', label: 'Home Loan' },
    { value: 'AUTO', label: 'Auto Loan' },
    { value: 'EDUCATION', label: 'Education Loan' }
  ];

  const termOptions = [
    { value: '6', label: '6 months' },
    { value: '12', label: '1 year' },
    { value: '24', label: '2 years' },
    { value: '36', label: '3 years' },
    { value: '48', label: '4 years' },
    { value: '60', label: '5 years' }
  ];

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });

    // Calculate EMI when amount, interest rate, or term changes
    if (name === 'amount' || name === 'interestRate' || name === 'termMonths') {
      calculateEMI({
        ...formData,
        [name]: value
      });
    }
  };

  const calculateEMI = (data) => {
    const { amount, interestRate, termMonths } = data;
    
    if (amount && interestRate && termMonths) {
      const principal = parseFloat(amount);
      const rate = parseFloat(interestRate) / 100 / 12; // Monthly rate
      const months = parseInt(termMonths);
      
      if (principal > 0 && rate > 0 && months > 0) {
        const emi = (principal * rate * Math.pow(1 + rate, months)) / 
                   (Math.pow(1 + rate, months) - 1);
        
        const totalAmount = emi * months;
        const totalInterest = totalAmount - principal;
        
        setEmiCalculation({
          emi: emi.toFixed(2),
          totalAmount: totalAmount.toFixed(2),
          totalInterest: totalInterest.toFixed(2)
        });
      } else {
        setEmiCalculation(null);
      }
    } else {
      setEmiCalculation(null);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    // Validate interest rate
    const rate = parseFloat(formData.interestRate);
    if (rate < 1 || rate > 50) {
      setError('Interest rate must be between 1% and 50%');
      setLoading(false);
      return;
    }

    try {
      const loanData = {
        ...formData,
        amount: parseFloat(formData.amount),
        interestRate: parseFloat(formData.interestRate),
        termMonths: parseInt(formData.termMonths)
      };

      await loanAPI.apply(user.customerId, loanData);
      
      setSuccess('Loan application submitted successfully! You will be notified once it\'s processed.');
      
      // Reset form
      setFormData({
        loanType: 'PERSONAL',
        amount: '',
        interestRate: '',
        termMonths: '12'
      });
      setEmiCalculation(null);
      
      // Redirect to dashboard after a delay
      setTimeout(() => {
        navigate('/dashboard');
      }, 3000);
    } catch (error) {
      setError(
        error.response?.data?.message || 
        'Failed to submit loan application. Please try again.'
      );
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount || 0);
  };

  return (
    <div className="container">
      <div className="card" style={{ maxWidth: '600px', margin: '2rem auto' }}>
        <div className="text-center">
          <h2 style={{ color: '#667eea', marginBottom: '2rem' }}>
            💰 Apply for Loan
          </h2>
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
            <label htmlFor="loanType">Loan Type</label>
            <select
              id="loanType"
              name="loanType"
              className="form-control"
              value={formData.loanType}
              onChange={handleChange}
              required
            >
              {loanTypes.map(type => (
                <option key={type.value} value={type.value}>
                  {type.label}
                </option>
              ))}
            </select>
          </div>

          <div className="grid grid-2" style={{ gap: '1rem' }}>
            <div className="form-group">
              <label htmlFor="amount">Loan Amount ($)</label>
              <input
                type="number"
                id="amount"
                name="amount"
                className="form-control"
                value={formData.amount}
                onChange={handleChange}
                required
                min="1000"
                max="1000000"
                placeholder="Enter loan amount"
              />
            </div>

            <div className="form-group">
              <label htmlFor="interestRate">
                Custom Interest Rate (%)
                <small style={{ display: 'block', color: '#666' }}>
                  Between 1% and 50%
                </small>
              </label>
              <input
                type="number"
                id="interestRate"
                name="interestRate"
                className="form-control"
                value={formData.interestRate}
                onChange={handleChange}
                required
                min="1"
                max="50"
                step="0.1"
                placeholder="Enter interest rate"
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="termMonths">Loan Term</label>
            <select
              id="termMonths"
              name="termMonths"
              className="form-control"
              value={formData.termMonths}
              onChange={handleChange}
              required
            >
              {termOptions.map(term => (
                <option key={term.value} value={term.value}>
                  {term.label}
                </option>
              ))}
            </select>
          </div>

          {/* EMI Calculator */}
          {emiCalculation && (
            <div className="card" style={{ 
              backgroundColor: '#f8f9fa', 
              border: '2px solid #667eea',
              marginBottom: '1.5rem'
            }}>
              <h4 style={{ color: '#667eea', marginBottom: '1rem' }}>
                📊 EMI Calculation
              </h4>
              <div className="grid grid-3" style={{ gap: '1rem', textAlign: 'center' }}>
                <div>
                  <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#28a745' }}>
                    {formatCurrency(emiCalculation.emi)}
                  </div>
                  <small style={{ color: '#666' }}>Monthly EMI</small>
                </div>
                <div>
                  <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#667eea' }}>
                    {formatCurrency(emiCalculation.totalAmount)}
                  </div>
                  <small style={{ color: '#666' }}>Total Amount</small>
                </div>
                <div>
                  <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#ffc107' }}>
                    {formatCurrency(emiCalculation.totalInterest)}
                  </div>
                  <small style={{ color: '#666' }}>Total Interest</small>
                </div>
              </div>
            </div>
          )}

          <button 
            type="submit" 
            className="btn" 
            style={{ width: '100%', marginBottom: '1rem' }}
            disabled={loading}
          >
            {loading ? 'Submitting Application...' : 'Submit Loan Application'}
          </button>
        </form>

        <div style={{ 
          marginTop: '2rem', 
          padding: '1rem', 
          backgroundColor: '#e9ecef', 
          borderRadius: '8px' 
        }}>
          <h5 style={{ color: '#667eea', marginBottom: '0.5rem' }}>
            📋 Application Requirements:
          </h5>
          <ul style={{ margin: '0.5rem 0', paddingLeft: '1.5rem', color: '#666' }}>
            <li>Minimum loan amount: $1,000</li>
            <li>Maximum loan amount: $1,000,000</li>
            <li>Interest rate: 1% - 50% (customizable)</li>
            <li>Valid account in good standing</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default LoanApplication;
