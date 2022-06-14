package com.example.miaprueba

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.miaprueba.databinding.ActivityInfoactiBinding

class InfoactiActivity : AppCompatActivity() {

    lateinit var binding: ActivityInfoactiBinding
    var nombre=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoactiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        cogerDatos()
        meterInfo()
        setListener()

    }

    private fun setListener() {

        binding.btnSalirInfo.setOnClickListener {
            var i = Intent(this, ActividadesActivity::class.java).apply {
                putExtra("nombre", "bodycombat")
            }
            startActivity(i)
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI() }


    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)      }


    private fun showSystemUI() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)       }

    private fun meterInfo() {

        Log.d("nombre => ", nombre)

        when(nombre){
            "bodycombat" -> {

                binding.imagePortada.setBackgroundResource(R.drawable.bodycombatacti)
                binding.txtTituloActi.text="ACTIVIDAD BODY COMBAT"
                binding.txtTextoActi.text="El body combat es un programa de ejercicios cardiovasculares que se realiza en grupo y consiste en realizar movimientos de diferentes artes marciales como el taekwondo, karate, capoeira, muay thay (boxeo tailandés), tai chi o boxeo, entre otros, a través de coreografías con música en la que se pueden ejercitar brazos, hombros, espalda, piernas y abdominales, permitiendo así mejorar la flexibilidad, la fuerza, la coordinación y la resistencia cardiovascular.\n\n" +
                        "El body combat es una actividad que cualquier persona puede realizar, independientemente de su edad, ya que permite adaptar los ejercicios a todas las condiciones físicas."

            }
            "hiit" -> {

                binding.imagePortada.setBackgroundResource(R.drawable.hiitacti)
                binding.txtTituloActi.text="ACTIVIDAD HIIT"
                binding.txtTextoActi.text="El HIIT es aplicable a diferentes actividades (Bicicleta, Pesas Rusas, Cinta, Entrenamiento en suspensión, etc…) y permite ir variando los ejercicios, los tiempos de descanso y actividad… Eso sí, algo que notarás sea cual sea tu actividad, es que el entrenamiento HIIT es muy concentrado durante el tiempo que dura la sesión, por lo que aprovecharás los minutos al máximo.\n\n" +
                        "Además, por lo que respecta a sus beneficios, el entrenamiento HIIT supone un trabajo más exigente para las capacidades cardiorespiratorias, mejorando nuestra resistencia de forma notable. Además numerosos estudios han demostrado su eficacia de cara a la pérdida de peso de manera eficiente y efectiva.\n\n" +
                        "Cabe señalar también que este tipo de entrenamiento es perfecto para aquellas personas que cuentan con un tiempo limitado, ya que en sesiones de 45 minutos o una hora se concentra todo el trabajo."

            }
            "cardio" -> {

                binding.imagePortada.setBackgroundResource(R.drawable.cardioacti)
                binding.txtTituloActi.text="ACTIVIDAD CARDIO"
                binding.txtTextoActi.text="A los ejercicios cardiovasculares se les conoce como cardio, refiriéndose al incremento del ritmo cardiaco que aceleran la respiración, queman más calorías y conforme se practican con frecuencia mejoran la resistencia. Además de quemar  calorías, el ejercicio cardio tiene beneficios visibles a corto plazo.\n\n" +
                        "Uno de los beneficios indiscutibles de hacer cardio es perder peso. Lo mejor de todo es que al hacer cardio regularmente el peso que se pierde es sostenido y esas libras de más serán más difíciles de recuperar. Para que este proyecto sea exitoso se debe tener una rutina cardio de al menos 30 minutos cada una, por cinco días a la semana. La clave está en la frecuencia, ya que es mejor hacer ejercicio varias veces a la semana, que hacer un solo día una jornada larga, para mantener una rutina  que mejore paulatinamente la condición física."

            }
            "fuerza" -> {

                binding.imagePortada.setBackgroundResource(R.drawable.fuerzaacti)
                binding.txtTituloActi.text="ACTIVIDAD FUERZA"
                binding.txtTextoActi.text="El ejercicio físico aporta numeroso beneficios para la salud de las personas y el entrenamiento con pesas por lo tanto también. Pero suele asociarse solamente a personas que buscan mejorar si estética corporal. Pero tiene muchos beneficios fuera de la estética.\n\n" +
                        "Para realizar el entrenamiento con pesas y aprovechar sus beneficios necesitamos conocer la técnica de cada ejercicio correctamente, si no realizamos una correcta ejecución de los movimiento podremos llegar a lesionarnos.\n\n" +
                        "El entrenamiento con pesas nos aporta grandes beneficios independientemente de ser hombre o mujer. Pero tenemos que realizarlo correctamente para no sufrir lesiones.\n\n" +
                        "Si estás empezando a entrenar es importante contar con un entrenador o alguien con conocimientos que pueda ayudarte en la técnica y postura de los ejercicios."

            }
            "libre" -> {

                binding.imagePortada.setBackgroundResource(R.drawable.abdominalesacti)
                binding.txtTituloActi.text="ACTIVIDAD ZONA LIBRE"
                binding.txtTextoActi.text="Esta zona se caracteriza por contar con un equipamiento consistente en barras, discos, mancuernas, bancos de distintos diseños en función de su finalidad, soportes, jaulas, etc.\n\n" +
                        "El uso del equipamiento de esta zona, requiera un poco más de experiencia que la de las máquinas selectorizadas, ya que los movimientos con peso libre, requieren mayor destreza y mayor control motor. Hay que tener en cuenta que además de los músculos motores primarios, trabajan los músculos sinergistas y fijadores, con el fin de estabilizar el peso. Esto lejos de ser un handicap es un valor añadido ya que cuando se tiene la suficiente experiencia en el entrenamiento de fuerza se consigue un trabajo mucho más aplicable a los movimientos globales y/o deportivos, debido a que no solo mejora la fuerza, sino también la coordinación y el equilibrio muscular.\n\n" +
                        "El entrenamiento con pesos libres permite un mayor abanico de ángulos y recorridos, pero a su vez exige un mayor control sobre el ejercicio."

            }
            "cycling" -> {

                binding.imagePortada.setBackgroundResource(R.drawable.ciclyngacti)
                binding.txtTituloActi.text="ACTIVIDAD CYCLING"
                binding.txtTextoActi.text="El cycling, o ciclo indoor, consiste en clases dirigidas en una bicicleta fija acompañadas de una música brutal. Eso es en esencia. Clases vibrantes, motivantes y llenas de energía. Es toda una experiencia donde las luces y la música juegan un papel primordial.\n\n" +
                        "El/la monitor/a, te guiará durante toda la clase, dándote las indicaciones que exige la coreografía o el recorrido diseñado. No tendrás que salir a la carretera o al bosque para recrear un tour fascinante, donde los cambios de intensidad de pedaleo, el aumento de la resistencia, los cambios de posición en la bici, irán acompañados en todo momento por temazos musicales y un juego de luces merecedor de las mejores discotecas."

            }
            "crossfit" -> {

                binding.imagePortada.setBackgroundResource(R.drawable.crossfitacti)
                binding.txtTituloActi.text="ACTIVIDAD CROSSFIT"
                binding.txtTextoActi.text="CrossFit se define como un sistema de entrenamiento de fuerza y acondicionamiento basado en ejercicios funcionales constantemente variados realizados a una alta intensidad. Esto significa que nos valemos de una gran cantidad de ejercicios y disciplinas deportivas (gimnasia, halterofilia, carrera…), de entre las cuales seleccionamos técnicas o movimientos aplicables a la vida diaria y los combinamos de muchas formas diferentes en entrenamientos intensos, resultando no solo un experiencia exigente durante la cual el carácter lúdico y la camaradería cobran un papel primordial, sino también un programa insuperable para desarrollar las diez capacidades físicas generales: resistencia cardiovascular, resistencia energética, fuerza, flexibilidad, potencia, velocidad, coordinación, agilidad, equilibrio y precisión.\n\n" +
                        "Gracias a su tremenda efectividad como sistema de preparación física, en sus orígenes el CrossFit fue elegido por numerosas academias militares, cuerpos de policía, artistas marciales y cientos de deportistas de élite en todo el mundo como programa de acondicionamiento y entrenamiento de fuerza estándar.\n" +
                        "\n" +
                        "No obstante, a día de hoy, el CrossFit se ha popularizado en todos los sectores de la población. El hecho de ser un programa diseñado para ser fácilmente adaptable lo convierte en el sistema de entrenamiento perfecto para cualquier persona con motivación, independientemente de su edad, sexo, capacidades o experiencia previa."

            }
            "yoga" -> {

                binding.imagePortada.setBackgroundResource(R.drawable.yogaacti)
                binding.txtTituloActi.text="ACTIVIDAD YOGA"
                binding.txtTextoActi.text="El yoga no es sólo ejercicio físico, sino una disciplina de origen milenario que ha evolucionado en el mundo occidental. Me gusta subrayar esto porque esta disciplina tiene muchas facetas y es difícil hablarte de todas en un solo post. Aun así, sobre todo si practicas yoga para principiantes, es posible que tengas curiosidad por saber más de esta disciplina milenaria. Te propongo este resumen para que empieces a conocerla.\n\n" +
                        "El yoga es una disciplina milenaria originaria de la India, mucho más que ejercicio físico. Hay que recordar que no es una religión, sino un camino espiritual para que mente, cuerpo y alma se unan. Los textos sagrados dicen que el objetivo de esta disciplina es conseguir una mente más tranquila y aunque cada uno se marque su propio objetivo este debería ser el principal. Cuando el yogui o yoguini decide avanzar en el camino del yoga, inicia un camino de evolución personal que le enseña a vivir de forma pacífica."

            }
            else -> {

            }
        }

    }

    private fun cogerDatos() {
        val bundle= intent.extras
        nombre = bundle?.getString("nombre").toString()
    }
}