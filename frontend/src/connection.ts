// import { add } from "date-fns";

const href = 'http://127.0.0.1:8080';

export const verify = async () => {
    const response = await fetch(`${href}/auth`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    if (response.ok) {
        // const data = await response.json();
        // localStorage.setItem("user", JSON.stringify(data));
        return true;
    } else {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        return false;
    }
}

export const login = async (email: string, password: string, setError: React.Dispatch<React.SetStateAction<string>>) => {
    const response = await fetch(`${href}/auth/login`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
    });
    
    if (response.ok) {
        const data = (await response.json()).data;
        localStorage.setItem("token", data.token);
        localStorage.setItem("user", JSON.stringify(data.user));
        return true;
    } else if (response.status === 401) {
        setError('Niepoprawny email lub hasło');
        return false;
    } else {
        setError('Coś poszło nie tak');
        return false;
    }
}

export const register = async (email: string, password: string, firstName: string, lastName: string, login: string, setError: React.Dispatch<React.SetStateAction<string>>) => {
    const response = await fetch(`${href}/auth/register`, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password, firstName, lastName, login }),
    });

    console.log(response);
    if (response.ok) {
        return true;
    } else if (response.status === 400) {
        const msg = (await response.json()).message;
        setError(msg);
        return false;
    } else {
        setError('Coś poszło nie tak');
        return false;
    }
}


export const getUser = async (id: string) => {
    const response = await fetch(`${href}/user/${id}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });
    if (response.ok) {
        const data = await response.json();
        return data;
    } else {
        return null;    
    }
}

export const getUsers = async (pageSize: Number, page: Number, sortBy: string, sortOrder: string) => {
    const response = await fetch(`${href}/user?page=${page}&size=${pageSize}&sort=${sortBy},${sortOrder}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    if (response.ok) {
        const data = await response.json();
        const users = {
            users: data.content,
            totalPages: data.totalPages,
            currentPage: data.pageable.pageNumber,
            pageSize: data.pageable.pageSize,
            totalUsers: data.totalElements,
            sortBy: sortBy,
            sortOrder: sortOrder
        }
        console.log(users);
        return users;
    } else {
        return null;
    }
}

export const getTeam = async (id: string) => {
    const response = await fetch(`${href}/teams/${id}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    if (response.ok) {
        const data = await response.json();
        return data;
    } else {
        return null;    
    }

}

export const getTeams = async (pageSize: Number, page: Number, sortBy: string, sortOrder: string) => {
    const response = await fetch(`${href}/teams?page=${page}&size=${pageSize}&sort=${sortBy},${sortOrder}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    if (response.ok) {
        const data = await response.json();
        const teams = {
            teams: data.content,
            totalPages: data.totalPages,
            currentPage: data.pageable.pageNumber,
            pageSize: data.pageable.pageSize,
            totalTeams: data.totalElements,
            sortBy: sortBy,
            sortOrder: sortOrder
        }
        console.log(teams);
        return teams;
    } else {
        return null;
    }
}

export const createTeam = async (name: string, description: string) => {
    const response = await fetch(`${href}/teams`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, description }),
    });

    if (response.ok) {
        const data = await response.json();
        return data;
    } else {
        return null;
    }
}

export const updateTeam = async (id: string, name: string, description: string) => {
    const response = await fetch(`${href}/teams/${id}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, description }),
    });
    if (response.ok) {
        const data = await response.json();
        return data;
    } else {
        return null;
    }
}

export const deleteTeam = async (id: string) => {
    const response = await fetch(`${href}/teams/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    if (response.ok) {
        return true;
    } else return false;
}

export const addTeamMember = async (teamId: string, email: string, setUserAlertMsg: React.Dispatch<any>) => {
    const response = await fetch(`${href}/teams/${teamId}/users`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json',
        },
        body: email,
    });

    if (response.ok) {
        setUserAlertMsg({
            variant: 'success',
            msg: 'pomyślnie dodano użytkownika do zespołu'
        });
        return true;
    } else if(response.status === 403) {
        setUserAlertMsg({
            variant: 'danger',
            msg: 'Użytkownik o podanym emailu nie istnieje'
        });
    } else {
        setUserAlertMsg({
            variant: 'danger',
            msg: 'Coś poszło nie tak przy próbie dodania użytkownika do zespołu'
        });
    }
}

export const removeTeamMember = async (teamId: string, userId: string) => {
    const response = await fetch(`${href}/teams/${teamId}/users/${userId}`, {
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

export const createTask = async (teamId: number, title: string, priority: string, deadline: string, userId: number, description: string) => {
    const response = await fetch(`${href}/tasks`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({title, description, priority, deadline, userId, teamId}),
    });

    if (response.ok) {
        const data = await response.json();
        console.log(data);
        return true;
    } else {
        return false;
    }
}

export const getTasks = async (teamId: string) => {
    const response = await fetch(`${href}/tasks/team/${teamId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    if (response.ok) {
        const data = await response.json();
        console.log(data);
        return data;
    } else {
        return null;
    }
}

export const getTask = async (id: string) => {
    const response = await fetch(`${href}/tasks/${id}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    if (response.ok) {
        const data = await response.json();
        return data;
    } else {
        return null;    
    }
}