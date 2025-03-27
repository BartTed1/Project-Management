const href = 'http://127.0.0.1:8080';

export const verify = async () => {
    const response = await fetch(`${href}/auth`, {
        method: 'GET',
        headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        }
    });

    console.log(response);
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