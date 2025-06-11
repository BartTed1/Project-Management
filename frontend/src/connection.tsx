// const href = 'http://192.168.1.2:3000';
const href = location.origin;

export const verify = async () => {
    const response = await fetch(`${href}/api/login`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    if (response.ok) {
        const data = await response.json();
        localStorage.setItem("user", JSON.stringify(data));
        return true;
    } else {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        return false;
    }
}

export const login = async (email: string, password: string, setError: React.Dispatch<React.SetStateAction<string>>) => {
    const response = await fetch(`${href}/api/login`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
    });
    
    if (response.ok) {
        const data = await response.json();
        localStorage.setItem("token", data.token);
        localStorage.setItem("user", JSON.stringify(data.user));
        return true;
    } else if (response.status === 400) {
        setError('Niepoprawny email lub hasło');
        return false;
    } else {
        setError('Coś poszło nie tak');
        return false;
    }
}

export const changeName = async (name: string) => {
    const response = await fetch(`${href}/api/users/name`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ name }),
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const changePassword = async (oldPassword: string, newPassword: string) => {
    const response = await fetch(`${href}/api/users/password`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ oldPassword, newPassword }),
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const getUsers = async (pageSize=10, page=1, sortBy="id", sortOrder="asc") => {
    const response = await fetch(`${href}/api/users?pageSize=${pageSize}&page=${page}&sortBy=${sortBy}&sortOrder=${sortOrder}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        alert('Coś poszło nie tak');
        return [];
    }
}

export const getUser = async (id: string) => {
    const response = await fetch(`${href}/api/users/${id}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else if (response.status === 404){
        alert('Nie znaleziono użytkownika');
        return false;
    } else {
        alert('Coś poszło nie tak');
        return false;
    }
}

export const getUserByEmail = async (email: string) => {
    const response = await fetch(`${href}/api/users/email/${email}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else if (response.status === 404){
        return 404;
    } else {
        return false;
    }
}

export const createUser = async (name: string, email: string, role: string) => {
    const response = await fetch(`${href}/api/users`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ name, email, role }),
    });

    if (response.ok) {
        return await response.json();
    } else if(response.status === 400){
        return 400;
    } else return false;
}

export const deleteUser = async (id: string) => {
    const response = await fetch(`${href}/api/users/${id}`, {
        method: 'DELETE',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const resetPassword = async (id: string) => {
    const response = await fetch(`${href}/api/users/${id}/password`, {
        method: 'PUT',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        return false;
    }
}

export const updateUser = async (id: string, name: string, role: string) => {
    const response = await fetch(`${href}/api/users/${id}`, {
        method: 'PUT',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ name, role }),
    });

    if(response.ok){
        return await response.json();
    }else{
        return false;
    }
}

export const getTeams = async (pageSize=10, page=1, sortBy="id", sortOrder="asc") => {
    const response = await fetch(`${href}/api/teams?pageSize=${pageSize}&page=${page}&sortBy=${sortBy}&sortOrder=${sortOrder}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        alert('Coś poszło nie tak');
        return [];
    }
}

export const getTeam = async (id: string) => {
    const response = await fetch(`${href}/api/teams/${id}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else if (response.status === 404){
        alert('Nie znaleziono zespołu');
        return false;
    } else {
        alert('Coś poszło nie tak');
        return false;
    }
}

export const createTeam = async (name: string, description: string) => {
    const response = await fetch(`${href}/api/teams`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ name, description }),
    });

    if (response.ok) {
        return await response.json();
    } else return false;
}

export const updateTeam = async (id: string, name: string, description: string) => {
    const response = await fetch(`${href}/api/teams/${id}`, {
        method: 'PUT',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({name, description})
    });

    if(response.ok){
        return await response.json();
    }else{
        return false;
    }
}

export const deleteTeam = async (id: string) => {
    const response = await fetch(`${href}/api/teams/${id}`, {
        method: 'DELETE',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const addTeamUser = async (teamId: string, userEmail: string) => {
    const usersEmail = [userEmail];
    const response = await fetch(`${href}/api/teams/${teamId}/users`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ usersEmail }),
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const removeTeamUser = async (teamId: string, userId: number) => {
    const response = await fetch(`${href}/api/teams/${teamId}/users/${userId}`, {
        method: 'DELETE',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const getTask = async (id: string) => {
    const response = await fetch(`${href}/api/tasks/${id}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else if (response.status === 404){
        alert('Nie znaleziono zadania');
        return false;
    } else {
        alert('Coś poszło nie tak');
        return false;
    }
}

export const createTask = async (teamId: number, title: string, priority: string, deadline: string, userId: number, description: string) => {
    const response = await fetch(`${href}/api/tasks`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ teamId, title, priority, deadline, userId, description }),
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const updateTask = async (id: number, title: string, priority: string, deadline: string, userId: number, description: string) => {
    const response = await fetch(`${href}/api/tasks/${id}`, {
        method: 'PUT',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ title, priority, deadline, userId, description }),
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const deleteTask = async (id: number) => {
    const response = await fetch(`${href}/api/tasks/${id}`, {
        method: 'DELETE',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const updateStatusTask = async (id: number, status: string) => {
    const response = await fetch(`${href}/api/tasks/${id}/status`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ status }),
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const createFile = async (teamId: number, file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('teamId', teamId.toString());

    const response = await fetch(`${href}/api/files`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: formData,
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const deleteFile = async (id: number) => {
    const response = await fetch(`${href}/api/files/${id}`, {
        method: 'DELETE',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const getMessages = async (teamId: number, pageSize=10, page=1) => {
    const response = await fetch(`${href}/api/messages/${teamId}?pageSize=${pageSize}&page=${page}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        return false;
    }
}

export const createMessage = async (teamId: number, content: string) => {
    const response = await fetch(`${href}/api/messages`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ teamId, content }),
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const refreshMessages = async (teamId: number, lastMessageId:  number) => {
    const response = await fetch(`${href}/api/messages/${teamId}/refresh/${lastMessageId}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        return false;
    }
}

export const getOldMessages = async (teamId: number, oldestMessageId:  number, limit: number) => {
    const response = await fetch(`${href}/api/messages/${teamId}/oldMessage/${oldestMessageId}?limit=${limit}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        return false;
    }
}

export const getRaport = async (teamId: number) => {
    const response = await fetch(`${href}/api/raport/${teamId}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.blob();
    } else {
        return false;
    }
}

export const getUnreadNotifications = async () => {
    const response = await fetch(`${href}/api/notifications/unread`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        return false;
    }
}

export const readNotification = async (id: number) => {
    const response = await fetch(`${href}/api/notifications/${id}`, {
        method: 'PUT',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },   
    });

    if (response.ok) {
        return true;
    } else {
        return false;
    }
}

export const refreshNotifications = async (lastId: number) => {
    const response = await fetch(`${href}/api/notifications/unread/${lastId}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        return false;
    }
}

export const getNotifications = async (pageSize=10, page=1) => {
    const response = await fetch(`${href}/api/notifications?pageSize=${pageSize}&page=${page}`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        return false;
    }
}