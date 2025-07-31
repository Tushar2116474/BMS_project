import axios from 'axios';

const API_BASE_URL = '/api/v1/bms';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if available
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Handle token expiration
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (credentials) => api.post('/login', credentials),
  register: (userData) => api.post('/register', userData),
};

export const customerAPI = {
  getProfile: (customerId) => api.get(`/customers/${customerId}`),
  updateProfile: (customerId, data) => api.put(`/customers/${customerId}`, data),
  getAccount: (customerId) => api.get(`/customers/${customerId}/account`),
  getLoans: (customerId) => api.get('/loans/applied'),
};

export const loanAPI = {
  apply: (customerId, loanData) => api.post('/loans/apply', loanData),
  getAll: (customerId) => api.get('/loans/applied'),
  getById: (customerId, loanId) => api.get(`/loans/${loanId}`),
};

export default api;
