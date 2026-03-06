import { createContext, useEffect, useState } from "react";
import { fetchFoodList } from "../service/foodService";



export const StoreContext =  createContext(null);

export const StoreContextProvider = (props) => {

    const [foodList, setFoodList] = useState([]);
    const [quantities, setQuantities] = useState({});

    const increaseQuantity = (foodId) => {
        setQuantities((prev) => ({
            ...prev, [foodId]: (prev[foodId] || 0) +1 
        })
        );
    }

    const decreaseQuantity = (foodId) => {
        setQuantities((prev) => ({
            ...prev, [foodId]: prev[foodId] > 0 ? prev[foodId] -1 : 0
        })
    );
    }

    const removeFromCart = (foodId) => {
        setQuantities((prev) => {
          const updateQuantities = {...prev};
          delete updateQuantities[foodId];
          return updateQuantities;
        })
    }

    
    const contextValue = {
        foodList,
        increaseQuantity,
        decreaseQuantity,
        quantities,
        removeFromCart,
    };

    useEffect(() => {
        async function loadData() {
            const data = await fetchFoodList();
            setFoodList(data);
        }
        loadData();
    }, [])

    return (
        <StoreContext.Provider value={contextValue}>
            {props.children}
        </StoreContext.Provider>
    )
}

