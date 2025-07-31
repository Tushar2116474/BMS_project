import React from 'react';
import { Link } from 'react-router-dom';

const Navbar = ({ user, onLogout }) => {
  return (
    <nav className="navbar">
      <div className="navbar-content">
        <Link to="/dashboard" className="navbar-brand">
          🏦 Bank Management System
        </Link>
        
        <div className="navbar-nav">
          <Link to="/dashboard" className="btn btn-secondary">
            Dashboard
          </Link>
          <Link to="/account" className="btn btn-secondary">
            Account
          </Link>
          <Link to="/apply-loan" className="btn btn-secondary">
            Apply Loan
          </Link>
          <span style={{ color: '#667eea', fontWeight: 'bold' }}>
            Welcome, {user?.firstName} {user?.lastName}
          </span>
          <button onClick={onLogout} className="btn btn-danger">
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
