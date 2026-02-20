import logo from './logo.png';
import cart from './cart.png';
import benne_dosa from './benne_dosa.png';
import desserts from './desserts.png';
import paneer_biriyani from './paneer_biriyani.png';
import pasta from './pasta.png';
import pizza from './pizza.png';
import salad from './salad.png';
import veg_roll from './veg_roll.png';

export const assets = {
    logo,
    cart,
}

export const categories = [
    {
        category: 'Biriyani',
        icon: paneer_biriyani
    },
    {
        category: 'South Indian',
        icon: benne_dosa
    },
    {
        category: 'Desserts',
        icon: desserts
    },
    {
        category: 'Pizza',
        icon: pizza
    },
    {
        category: 'Pasta',
        icon: pasta
    },
    {
        category: 'Salad',
        icon: salad
    },
    {
        category: 'Kolkata roll',
        icon: veg_roll
    }
]