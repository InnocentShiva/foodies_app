import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import './ListFood.css';
import { deleteFood, getFoodList } from '../../services/foodService';



const ListFood = () => {
  const [list, setList] = useState([]);
  const fetchList = async() => {
      try{
        const data = await getFoodList();
        setList(data);
      } catch (error) {
        toast.error('Failed to fetch food list. Error: ' + error.message);
      }
  }

  const removeFood = async (foodId) => { 
    try{
      const success = await deleteFood(foodId);
      if (success) {
        toast.success('Food deleted successfully.');
        await fetchList(); // Refresh the list after deletion
      }else {
        toast.error('Failed to delete food. Please try again.');
      }
    } catch (error) {
      toast.error('Failed to delete food. Error: ' + error.message);
    }
  }
  // This below function will help to call the fetchList function as soon as the component is loaded in the browser.
  useEffect(() => {          
    fetchList();
  }, []); // The empty array as the second argument ensures that the effect runs only once when the component mounts.

  return (
    <div className="py-5 row justify-content-center">
      <div className="col-11 card">
        <table className='table'> 
          <thead>
            <tr>
              <th>Image</th>
              <th>Name</th>
              <th>Category</th>
              <th>Price</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {
              list.map((item, index) => {
                return (
                  <tr key={index}>
                    <td>
                      <img src={item.imageUrl} alt="" height={48} width={48} />
                    </td>
                    <td>{item.name}</td>
                    <td>{item.category}</td>
                    <td>&#8377;{item.price}.00</td>
                    <td className='text-danger'>
                      <i className='bi bi-x-circle-fill' onClick={() => removeFood(item.id)}></i>
                    </td>
                  </tr>
                )
              })
          }
          </tbody>
        </table>
      </div>
    </div> 

  )
}

export default ListFood;