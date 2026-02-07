import { useState } from 'react';
import JewelryForm from './components/JewelryForm';
import JewelryList from './components/JewelryList';

function App() {
  const [refresh, setRefresh] = useState(false);

  const handleAdd = () => {
    setRefresh(!refresh); // Triggers list reload
  };

  return (
    <div style={{ backgroundColor: '#121212', minHeight: '100vh', padding: '40px 20px', fontFamily: 'Arial, sans-serif' }}>
      
      {/* HEADER SECTION - FIXED VISIBILITY */}
      <header style={{ textAlign: 'center', marginBottom: '50px' }}>
        <h1 style={{ 
            color: '#d4af37',   // GOLD COLOR (Fixed)
            fontFamily: 'serif', 
            fontSize: '3.5rem', 
            margin: '0', 
            textTransform: 'uppercase', 
            letterSpacing: '3px',
            display: 'flex', 
            alignItems: 'center', 
            justifyContent: 'center', 
            gap: '15px'
        }}>
          💎 Custom Jewelry Designer
        </h1>
      </header>
      
      <div style={{ maxWidth: '1200px', margin: '0 auto' }}>
        <JewelryForm onAdd={handleAdd} />
        <JewelryList key={refresh} />
      </div>
    </div>
  );
}

export default App;