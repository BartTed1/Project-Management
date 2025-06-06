import './style/App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Login from './pages/Login';
import Register from './pages/Register';
import NavigationBar from './components/NavigationBar';
import { verify } from './connection';
import Settings from './pages/Settings';
import Users from './pages/Users';
import Teams from './pages/Teams';
import OwnTeams from './pages/OwnTeams';
import Addteams from './pages/AddTeams';
import Team from './pages/Team';

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
          <Route path='/' element={<h1>Home</h1>} />
          <Route path='settings' element={<Settings />} /> 
          <Route path='users' element={<Users />} /> 
          <Route path='teams' element={<Teams />} />
          <Route path='own-teams' element={<OwnTeams />} />
          <Route path='addteams' element={<Addteams />} />
          <Route path='teams/:id' element={<Team />} />
          <Route path='*' element={<h1>404 Not Found</h1>} />
        </Routes>
      </Router>
    </>
  )
}

export default App
