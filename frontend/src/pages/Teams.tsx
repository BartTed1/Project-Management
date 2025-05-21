import { useEffect, useState } from "react";
import { getTeams } from "../connection";
import { Table, Container, Pagination } from "react-bootstrap";
import '../style/table.css';

const Teams = () => {
    const [data, setData] = useState(null as any);
    const urlParams = new URLSearchParams(window.location.search);
    const sortBy = urlParams.get('sortBy') || 'id'; 
    const sortOrder = urlParams.get('sortOrder') || 'asc';
    const page = parseInt(urlParams.get('page') || '0', 10);
    const pageSize = parseInt(urlParams.get('pageSize') || '10', 10);
    const userLogged = JSON.parse(localStorage.getItem('user') as string)

    useEffect(() => {
        const fetchUsers = async () => {
            const data = await getTeams(pageSize, page, sortBy, sortOrder);
            setData(data);  
            if(data === null){
                return;
            }
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

    const sortByDescription = () => {
        if(sortBy === 'description'){
            urlParams.set('sortOrder', sortOrder === 'asc' ? 'desc' : 'asc');
        }else{
            urlParams.set('sortBy', 'description');
            urlParams.set('sortOrder', 'asc');
        }
        window.history.replaceState({}, '', `${window.location.pathname}?${urlParams.toString()}`);
        location.reload();
    }

    if(!data)return <></>;

    return (
        <>
            <Container>
                <h1 className='my-4'>Zespoły</h1>
                {userLogged.role != "SUPERADMIN" && <a href='/addteams' className='btn btn-primary mb-4'>Utwórz zaspół</a>}
                <div className="table-responsive">
                    <Table striped bordered hover variant='dark'>
                        <thead className='thead text-center'>
                            <tr>
                                <th onClick={sortById}>#</th>
                                <th onClick={sortByName}>Nazwa</th>
                                <th onClick={sortByDescription}>opis</th>
                                <th>nazwa właściciela</th>
                                <th>email właściciela</th>
                            </tr>
                        </thead>
                        <tbody>
                            {data.teams.map((team: any) => (
                                <tr key={team.id}>
                                    <td>{team.id}</td>
                                    <td>{team.name}</td>
                                    <td>{team.description}</td>
                                    <td>{team.owner.name}</td>
                                    <td>{team.owner.email}</td>
                                </tr>
                            ))}
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

export default Teams;