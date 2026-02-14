import React from 'react';
import axios from 'axios';
import { assets } from '../../assets/assets';
import { useState } from 'react';
import { addFood } from '../../services/foodService';
import { toast } from 'react-toastify';

const AddFood = () => {

    const [image, setImage] = useState(false);
    const [data, setData] = useState({
        name: '',
        description: '',
        category: 'dosa',
        price: ''
    });
        
    
    
    

    const onChangeHandler = (event) => {
        const name = event.target.name;
        const value = event.target.value;

        setData(data => ({
            ...data,
            [name]: value
        }));
    }

    const onSubmitHandler = async (event) => {
        event.preventDefault();
        if (!image) {
            toast.error('Please select an image');
            return;
        }
    
        try {
            await addFood(data, image); 
            toast.success('Food added successfully.');
            setData({
                name: '',
                description: '',
                category: 'dosa',
                price: ''
            });
            setImage(null);
        } catch (error) {
            toast.error('Failed to add food. Error: ' + error.message);
        }
    
    
    }
  return (
    <div className="mt-2">
    <div className="row">
        <div className="card col-md-8 col-lg-6">
            <div className="card-body">    
                <form className="add-food-form" onSubmit={onSubmitHandler} >
                    <h3 className="text-center mb-4">Add Food</h3>
                    <div className="mb-3">
                        <label htmlFor="image"  className="form-label">
                            <img src={image ? URL.createObjectURL(image) : assets.upload} alt="upload Image" width={98}/>
                        </label>
                        <input type="file" className="form-control" id="image"  hidden onChange={(event) => setImage(event.target.files[0])}/>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="name" className="form-label">Name</label>
                        <input type="text" className="form-control" placeholder='Mysuru Dosa' id="name" required name='name' onChange={onChangeHandler} value={data.name}/>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="description" className="form-label">Description</label>
                        <textarea className="form-control" placeholder="Write content here..." id="description" rows="5" required name='description' onChange={onChangeHandler} value={data.description}></textarea>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="category" className="form-label">Category</label>
                        <select name='category'  id='category'  className="form-control" onChange={onChangeHandler} value={data.category}>
                            <option value="Burger">Dosa</option>
                            <option value="Pizza">Pizza</option>
                            <option value="Pasta">Pasta</option>
                            <option value="Salad">Salad</option>
                            <option value="Dessert">Dessert</option>
                            <option value="Roll">Roll</option>
                            <option value="Veg Biriyani">Veg Biriyani</option>
                        </select>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="price" className="form-label">Price</label>
                        <input type="number" name='price' placeholder="&#8377;200" id="price" className="form-control" onChange={onChangeHandler} value={data.price}/>
                    </div>
                    <div className="d-grid">
                        <button type="submit" className="btn btn-primary btn-lg">Save</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
  )
}

export default AddFood;