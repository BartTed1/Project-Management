import { useEffect, useState } from 'react';
import { Table, Container, Pagination, Alert} from 'react-bootstrap'
import { deleteUser, getUsers, resetPassword } from '../connection';
import infoIcon from '../assets/info.svg';
import deleteIcon from '../assets/delete.svg';
import restoreIcon from '../assets/restore.svg';
import editIcon from '../assets/edit.svg';
import '../style/table.css';

const Users = () => {
    const [alertMsg, setAlertMsg] = useState(null as any);
    const [data, setData] = useState(null as any);
    const userLogged = JSON.parse(localStorage.getItem('user') as string)
    const urlParams = new URLSearchParams(window.location.search);
    const sortBy = urlParams.get('sortBy') || 'id'; 
    const sortOrder = urlParams.get('sortOrder') || 'asc';
    const page = parseInt(urlParams.get('page') || '1', 10);
    const pageSize = parseInt(urlParams.get('pageSize') || '10', 10);

    useEffect(() => {
        const fetchUsers = async () => {
            const data = await getUsers(pageSize, page, sortBy, sortOrder);
            setData(data);  
            if(page > data.totalPages && data.totalPages > 0){
                urlParams.set('page', '1');
                window.history.replaceState({}, '', `${window.location.pathname}?${urlParams.toString()}`);
                location.reload();
            }
        };
        fetchUsers();
    }, []);

    const handlePageChange = (pageNumber: number) => {
        urlParams.set('page', pageNumber.toString());
        window.history.replaceState({}, '', `${window.location.pathname}?${urlParams.toString()}`);
        location.reload();
    };

    const sortById = () => {
        if(sortBy === 'id'){
            urlParams.set('sortOrder', sortOrder === 'asc' ? 'desc' : 'asc');
        }else{
            urlParams.set('sortBy', 'id');
            urlParams.set('sortOrder', 'asc');
        }
        window.history.replaceState({}, '', `${window.location.pathname}?${urlParams.toString()}`);
        location.reload();
    }

    const sortByName = () => {
        if(sortBy === 'name'){
            urlParams.set('sortOrder', sortOrder === 'asc' ? 'desc' : 'asc');
        }else{
            urlParams.set('sortBy', 'name');
            urlParams.set('sortOrder', 'asc');
        }
        window.history.replaceState({}, '', `${window.location.pathname}?${urlParams.toString()}`);
        location.reload();
    }

    const sortByEmail = () => {
        if(sortBy === 'email'){
            urlParams.set('sortOrder', sortOrder === 'asc' ? 'desc' : 'asc');
        }else{
            urlParams.set('sortBy', 'email');
            urlParams.set('sortOrder', 'asc');
        }
        window.history.replaceState({}, '', `${window.location.pathname}?${urlParams.toString()}`);
        location.reload();
    }

    const sortByRole = () => {
        if(sortBy === 'role'){
            urlParams.set('sortOrder', sortOrder === 'asc' ? 'desc' : 'asc');
        }else{
            urlParams.set('sortBy', 'role');
            urlParams.set('sortOrder', 'asc');
        }
        window.history.replaceState({}, '', `${window.location.pathname}?${urlParams.toString()}`);
        location.reload();
    }

    const handleDelete = async (id: number) => {
        if(window.confirm('Czy na pewno chcesz usunąć użytkownika?')){
            if(await deleteUser(id.toString())){
                setAlertMsg({
                    variant: 'success',
                    msg: 'pomyślnie usunięto użytkownika'
                });
                const newData = {
                    ...data,
                    users: data.users.filter((user: any) => user.id !== id)
                };
                setData(newData);
            }else{
                setAlertMsg({
                    variant: 'danger',
                    msg: 'Coś poszło nie tak przy próbie usunięcia użytkownika'
                });
            }
        }
    }

    const handleRestore = async (id: number) => {
        if(window.confirm('Czy na pewno chcesz zresetować hasło użytkownika?')){
            const password = await resetPassword(id.toString())
            if(password){
                setAlertMsg({
                    variant: 'success',
                    msg: `pomyślnie zresetowano hasło użytkownika nowe hasło to: ${password}`
                });
            }else{
                setAlertMsg({
                    variant: 'danger',
                    msg: 'Coś poszło nie tak przy próbie zresetowania hasła użytkownika'
                });
            }
        }
    }

    if(!data)return <></>;

   
    return (
        <>
            <Container>
                <h1 className='my-4'>Użytkownicy</h1>
                {userLogged.role != 'USER' && <a href='/adduser' className='btn btn-primary mb-4'>Dodaj użytkownika</a>}
                {alertMsg && <Alert variant={alertMsg.variant}>{alertMsg.msg}</Alert>}
                <div className="table-responsive">
                    <Table striped bordered hover variant='dark'>
                        <thead className='thead text-center'>
                            <tr>
                                <th onClick={sortById}>#</th>
                                <th onClick={sortByName}>Nazwa</th>
                                <th onClick={sortByEmail}>Email</th>
                                <th onClick={sortByRole}>Rola</th>
                                <th>Akcje</th>
                            </tr>
                        </thead>
                        <tbody>
                            {data.users.map((user: any) => {
                                return <tr key={user.id}>
                                    <td>{user.id}</td>
                                    <td>{user.name}</td>
                                    <td>{user.email}</td>
                                    <td>{user.role}</td>
                                    <td>
                                        <a href={`/users/${user.id}`}><img src={infoIcon} alt='info' width='20px' title='szczegóły' /></a>
                                        {((userLogged.role != 'USER' && user.role == 'USER') || (userLogged.role == 'SUPERADMIN' && user.role == 'ADMIN')) &&
                                            <>
                                                <img src={deleteIcon} alt='del' width='20px' title='usuń' className='icon' onClick={() => handleDelete(user.id)} />
                                                <img src={restoreIcon} alt='res' width='20px' title='zresetuj hasło' className='icon' onClick={() => handleRestore(user.id)}  />
                                                <a href={`/users/${user.id}/edit`}><img src={editIcon} alt='edit' width='20px' title='edytuj' /></a>
                                            </>
                                        }
                                    </td>
                                </tr>
                            })}
                        </tbody>
                    </Table>
                </div>
                {data.totalPages > 1 &&
                    <Pagination className="justify-content-center">
                        <Pagination.First onClick={() => handlePageChange(1)} disabled={page === 1} />
                        <Pagination.Prev onClick={() => handlePageChange(page - 1)} disabled={page === 1} />
                        {[...Array(data.totalPages)].map((_, index) => (
                        <Pagination.Item
                            key={index + 1}
                            active={index + 1 === page}
                            onClick={() => handlePageChange(index + 1)}
                            className="bg-dark text-white"
                        >
                            {index + 1}
                        </Pagination.Item>
                        ))}
                        <Pagination.Next onClick={() => handlePageChange(page + 1)} disabled={page === data.totalPages} />
                        <Pagination.Last onClick={() => handlePageChange(data.totalPages)} disabled={page === data.totalPages} />
                    </Pagination>
                }
            </Container>
        </>
    );
}

export default Users