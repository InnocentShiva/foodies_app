import axios from "axios";

const API_BASE_URL = 'http://localhost:8080/api/foods';

export const addFood = async (data, image) => {
    if (!image) {
        alert('Please select an image');
        return;
        }
        
    const formData = new FormData();
    formData.append('food', JSON.stringify(data));
    formData.append('file', image);

    try {
        await axios.post(API_BASE_URL, formData, {headers: {'Content-type': 'multipart/form-data'}});
        
    } catch (error) {
        
        throw error;
    }
}

export const getFoodList = async() => {
    try {
         const response = await axios.get(API_BASE_URL);
    
    return response.data;
    } catch (error) {
        console.log('Error fetching food list:', error);
            throw error;
    }
}

export const deleteFood = async (foodId) => {
    try{
        const response = await axios.delete(`${API_BASE_URL}/${foodId}`);
        return response.status === 204;
    } catch (error) {
        console.log('Error deleting food:', error);
        throw error;
    }
}