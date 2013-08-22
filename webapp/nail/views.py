from flask import render_template

from nail import app

@app.route('/')
def index():
    return render_template('index.html', game_state='start', leaderboard=
        [
            { 
                'name' : 'Clank26',
                'total' : 20,
                'won' : 20,
                'kills' : 8,
            },

            { 
                'name' : 'PostVillageCore',
                'total' : 18,
                'won' : 14,
                'kills' : 9,
                'op' : True,
            },

            { 
                'name' : 'xXx_killer69_xXx_',
                'total' : 10,
                'won' : 4,
                'kills' : '9001',
            },
        ])
