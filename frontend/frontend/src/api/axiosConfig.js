import axios from 'axios';

export default axios.create({
    // Make sure it looks exactly like this!
    baseURL: 'https://jewelry-backend-jccu.onrender.com/jewelry', 
    headers: {
        'Content-Type': 'application/json'
    }
});