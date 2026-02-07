import axios from 'axios';

const api = axios.create({
    baseURL: 'https://jewelry-backend-jccu.onrender.com', // This points to your Java Backend
    headers: {
        'Content-Type': 'application/json',
    }
});

export default api;