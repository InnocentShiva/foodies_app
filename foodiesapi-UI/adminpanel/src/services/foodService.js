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