import axios from 'axios';

export default axios.create({
    // ⚠️ IMPORTANT: Paste your Render URL here, but DO NOT add /jewelry at the end.
    // It should look like: https://jewelry-backend-xxxx.onrender.com
    baseURL: 'https://jewelry-backend-REPLACE_WITH_YOUR_ID.onrender.com', 
    headers: {
        'Content-Type': 'application/json'
    }
});