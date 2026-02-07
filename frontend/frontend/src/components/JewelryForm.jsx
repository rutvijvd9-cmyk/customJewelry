import { useState } from 'react';
import api from '../api/axiosConfig';

const JewelryForm = ({ onAdd }) => {
    const [formData, setFormData] = useState({
        name: '',
        type: '',
        material: '',
        aiImagePrompt: ''
    });
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        // 1. Handle Empty Fields: Create defaults if user typed nothing
        const payload = { ...formData };
        if (!payload.name.trim()) payload.name = "Mystery Creation";
        if (!payload.type.trim()) payload.type = "Custom Piece";
        if (!payload.material.trim()) payload.material = "Precious Metal";

        try {
            const response = await api.post('/jewelry', payload);
            if (onAdd) onAdd(response.data);
            
            // Clear form
            setFormData({ name: '', type: '', material: '', aiImagePrompt: '' });
        } catch (error) {
            console.error("Error creating jewelry:", error);
            alert("Error creating design.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ 
            background: '#1e1e1e', padding: '30px', borderRadius: '12px', 
            border: '1px solid #333', marginBottom: '40px', color: '#e0e0e0',
            boxShadow: '0 4px 15px rgba(0,0,0,0.3)'
        }}>
            <h2 style={{ color: '#d4af37', marginTop: 0, textTransform: 'uppercase', letterSpacing: '1px' }}>
                Create New Design
            </h2>
            <p style={{ fontSize: '0.9em', color: '#888', marginBottom: '20px' }}>
                Fields are optional. Leave them blank to let the AI surprise you!
            </p>

            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '15px' }}>
                    <input name="name" placeholder="Name" value={formData.name} onChange={handleChange} style={inputStyle} />
                    <input name="type" placeholder="Type" value={formData.type} onChange={handleChange} style={inputStyle} />
                    <input name="material" placeholder="Material" value={formData.material} onChange={handleChange} style={inputStyle} />
                </div>

                <textarea 
                    name="aiImagePrompt" 
                    placeholder="Describe your dream jewelry... (or leave empty)" 
                    value={formData.aiImagePrompt} 
                    onChange={handleChange} 
                    style={{ ...inputStyle, height: '80px', resize: 'none' }} 
                />

                <button 
                    type="submit" 
                    disabled={loading}
                    style={{
                        padding: '12px', background: loading ? '#555' : '#d4af37', 
                        color: loading ? '#ccc' : '#000', border: 'none', borderRadius: '6px', 
                        fontWeight: 'bold', fontSize: '1rem', cursor: loading ? 'not-allowed' : 'pointer'
                    }}
                >
                    {loading ? '✨ Generating...' : 'Create Design'}
                </button>
            </form>
        </div>
    );
};

const inputStyle = {
    padding: '12px', background: '#2a2a2a', border: '1px solid #444',
    borderRadius: '6px', color: 'white', fontSize: '0.95rem'
};

export default JewelryForm;