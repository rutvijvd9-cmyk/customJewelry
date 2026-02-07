import { useEffect, useState } from 'react';
import api from '../api/axiosConfig';

const JewelryList = () => {
    const [jewelry, setJewelry] = useState([]);
    const [regeneratingId, setRegeneratingId] = useState(null);

    const fetchJewelry = async () => {
        try {
            const response = await api.get('/jewelry');
            console.log("API Response:", response.data); // Debugging log
            // FIX 1: Ensure we always have an array, even if backend returns null
            setJewelry(response.data || []);
        } catch (error) {
            console.error("Error fetching jewelry:", error);
            setJewelry([]); // Fallback to empty list on error
        }
    };

    useEffect(() => {
        fetchJewelry();
    }, []);

    const handleDelete = async (id) => {
        if(!window.confirm("Are you sure?")) return;
        try {
            await api.delete(`/jewelry/${id}`);
            setJewelry(jewelry.filter(item => item.id !== id));
        } catch (error) {
            alert("Could not delete item.");
        }
    };

    const handleEditImage = async (item) => {
        const newPrompt = prompt("Edit the image description:", item.aiImagePrompt);
        if (newPrompt === null || newPrompt === item.aiImagePrompt) return;

        setRegeneratingId(item.id);
        try {
            // NOTE: Ensure your backend handles plain text if you use this header
            const response = await api.put(`/jewelry/${item.id}/image`, newPrompt, {
                headers: { 'Content-Type': 'text/plain' } // Check if your backend expects JSON or String
            });
            // Update the specific item in the list
            setJewelry(jewelry.map(j => (j.id === item.id ? response.data : j)));
        } catch (error) {
            console.error("Error updating image:", error);
            alert("Failed to update image.");
        } finally {
            setRegeneratingId(null);
        }
    };

    return (
        <div style={{ marginTop: '40px' }}>
            <h2 style={{ borderBottom: '1px solid #333', paddingBottom: '10px', color: '#d4af37' }}>
                My Collection
            </h2>
            
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '25px' }}>
                {/* FIX 2: Use optional chaining (?.) to prevent crash if jewelry is null */}
                {jewelry?.length > 0 ? (
                    jewelry.map((item) => (
                        <div key={item.id} className="jewelry-card" style={{ 
                            border: '1px solid #333', borderRadius: '12px', padding: '25px', 
                            background: '#1e1e1e', position: 'relative', boxShadow: '0 4px 6px rgba(0,0,0,0.5)', color: '#e0e0e0'
                        }}>
                            
                            {/* IMAGE SECTION */}
                            <div style={{ height: '250px', background: '#000', borderRadius: '8px', marginBottom: '10px', position: 'relative', overflow: 'hidden' }}>
                                {item.imageUrl ? (
                                    <img 
                                        src={item.imageUrl} 
                                        alt={item.name} 
                                        style={{ width: '100%', height: '100%', objectFit: 'cover', opacity: regeneratingId === item.id ? 0.5 : 1 }} 
                                    />
                                ) : (
                                    <div style={{ width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#555' }}>No Image</div>
                                )}

                                <button 
                                    onClick={() => handleEditImage(item)}
                                    disabled={regeneratingId === item.id}
                                    style={{ 
                                        position: 'absolute', bottom: '10px', right: '10px',
                                        background: 'rgba(0,0,0,0.7)', color: 'white', 
                                        border: '1px solid white', borderRadius: '20px', 
                                        padding: '5px 15px', cursor: 'pointer',
                                        backdropFilter: 'blur(2px)'
                                    }}
                                >
                                    {regeneratingId === item.id ? '↻ Loading...' : '✎ Edit Image'}
                                </button>
                            </div>

                            <button 
                                onClick={() => handleDelete(item.id)}
                                style={{ position: 'absolute', top: '15px', right: '15px', background: 'transparent', color: '#666', border: '1px solid #444', borderRadius: '50%', width: '30px', height: '30px', cursor: 'pointer' }}
                            >✕</button>

                            <h3 style={{ marginTop: '0', fontSize: '1.5em', color: '#d4af37' }}>{item.name}</h3>
                            <p style={{color: '#aaa', fontSize: '0.9em'}}>{item.type} | {item.material}</p>
                        </div>
                    ))
                ) : (
                    <div style={{ color: '#777', gridColumn: '1 / -1', textAlign: 'center', padding: '20px' }}>
                        No designs found yet. Start creating above!
                    </div>
                )}
            </div>
        </div>
    );
};

export default JewelryList;