import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Login from './pages/login';
import { verify } from './connection';
import './style/App.css';
import NavigationBar from './components/NavigationBar';
import Settings from './pages/Settings';
import Users from './pages/users';
import User from './pages/user';
import AddUser from './pages/addUser';
import EditUser from './pages/editUser';
import Teams from './pages/teams';
import OwnTeams from './pages/ownTeams';
import Addteams from './pages/addTeams';
import Team from './pages/team';
import EditTeams from './pages/editTeams';
import AddTask from './pages/addTask';
import EditTask from './pages/editTask';
import Task from './pages/task';
import Notifications from './pages/notifications';
import Home from './pages/home';

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
    return <Login />;
  }

  return (
    <Router>
      <NavigationBar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="settings" element={<Settings />} />
        <Route path="users" element={<Users />} />
        <Route path="users/:id" element={<User />} />
        <Route path="adduser" element={<AddUser />} />
        <Route path="users/:id/edit" element={<EditUser />} />
        <Route path="own-teams" element={<OwnTeams />} />
        <Route path="teams" element={<Teams />} />
        <Route path="teams/:id" element={<Team />} />
        <Route path="addteams" element={<Addteams />} />
        <Route path="teams/:id/edit" element={<EditTeams />} />
        <Route path="teams/:id/addTask" element={<AddTask />} />
        <Route path="tasks/:id" element={<Task />} />
        <Route path="tasks/:id/edit" element={<EditTask />} />
        <Route path="notifications" element={<Notifications />} />
        <Route path="*" element={<h1>Not Found</h1>} />
      </Routes>
    </Router>
  );
}

export default App;