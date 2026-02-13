import React from 'react'
import { Routes, Route } from 'react-router-dom'
import Sidebar from './components/sidebar/Sidebar'
import Menubar from './components/menubar/Menubar'
import AddFood from './pages/AddFood/AddFood'
import ListFood from './pages/ListFood/ListFood'
import Orders from './pages/Orders/Orders'

const App = () => {
  return (
    <div className="d-flex" id="wrapper">
            
            <Sidebar />
            
            <div id="page-content-wrapper">
                
                <Menubar />
                
                <div className="container-fluid">
                    <Routes>
                      <Route path='/add' element={<AddFood />} />
                      <Route path='/list' element={<ListFood />} />
                      <Route path='/orders' element={<Orders />} />
                      <Route path='/' element={<ListFood />}  />
                    </Routes>
                </div>
            </div>
        </div>
  )
}

export default App