import './style/App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Login from './pages/Login';
import Register from './pages/Register';
import NavigationBar from './components/NavigationBar';
import { verify } from './connection';
import Settings from './pages/Settings';

function App() {
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const checkToken = async () => {
      if (localStorage.getItem('token')) {
        const result = await verify();
        setIsAuthenticated(result);
      }
      setLoading(false);
    };

    checkToken();
  }, []);

  if (loading) {
    return <div className="loading-container"><div className="loader"></div></div>;
  }

  if (!isAuthenticated) {
    return (
      <>
        <Router>
          <Routes>
            <Route path="register" element={<Register />} />
            <Route path="*" element={<Login />} />         
          </Routes>
        </Router>
      </>
    )
  }

  return (
    <>
      <Router>
        <NavigationBar />
        <Routes>
          <Route path='settings' element={<Settings />} /> 
        </Routes>
      </Router>
    </>
  )
}

export default App
