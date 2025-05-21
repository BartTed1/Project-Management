import React from 'react';

interface PriorityProps {
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
}

const Priority: React.FC<PriorityProps> = ({ priority }) => {
  let backgroundColor: string;
  let label: string;

  switch (priority) {
    case 'LOW':
      backgroundColor = 'green';
      label = 'niski';
      break;
    case 'MEDIUM':
      backgroundColor = 'orange';
      label = 'średni';
      break;
    case 'HIGH':
      backgroundColor = 'red';
      label = 'wysoki';
      break;
    default:
      backgroundColor = 'gray';
      label = 'nieznany';
  }

  const style: React.CSSProperties = {
    backgroundColor,
    color: 'white',
    padding: '4px 8px',
    borderRadius: '4px',
    display: 'inline-block',
    textAlign: 'center' as 'center',
    fontWeight: 'bold',
    fontSize: '12px'
  };

  return (
    <div style={style}>
      {label}
    </div>
  );
};

export default Priority;