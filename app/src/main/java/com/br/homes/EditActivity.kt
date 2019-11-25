package com.br.homes

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.br.homes.Util.*
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ir.apend.slider.model.Slide
import kotlinx.android.synthetic.main.activity_edit.*
import thiagocury.eti.br.exconsumindoviacepkotlinretrofit2.CEP
import thiagocury.eti.br.exconsumindoviacepkotlinretrofit2.RetrofitInitializer
import java.io.ByteArrayOutputStream
import java.util.*

class EditActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()
    var databaseReference: DatabaseReference? = null
    var selectPhotoUri: Uri? = null
    var selected = propertyUtilAll.get(0).url
    var randomPath = ""
    var position_p: String? = null
    var option: String? = null
    var randomID = propertyUtilAll.get(0).random!!
    var count = 0
    var count1 = 0
    lateinit var downloadUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)

        setValues()

        val smf = SimpleMaskFormatter("NNNNN-NNN")
        val mtw = MaskTextWatcher(txt_cep, smf)
        txt_cep.addTextChangedListener(mtw)

        val smf1 = SimpleMaskFormatter("(NN) NNNNN-NNNN")
        val mtw1 = MaskTextWatcher(txt_phone, smf1)
        txt_phone.addTextChangedListener(mtw1)

        pesquisar.setOnClickListener {
            if (txt_cep.text.toString().length == 9) {
                searchCEP()
            } else {
                Toast.makeText(this, "CEP inválido!", Toast.LENGTH_SHORT).show()
            }
        }
        image1.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent, 0)
        }
        val types_property = arrayOf("Apartamento", "Casa comum")
        val types_transaction = arrayOf("Alugar", "Vender")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            types_property
        )
        val adapter1 = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            types_transaction
        )
        adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
        spinner!!.adapter = adapter

        adapter1.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1)
        transacao!!.adapter = adapter1


        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>, view: View,
                    position: Int, id: Long
                ) {
                    if (position == 0) {
                        position_p = "Apartamento"
                    } else {
                        position_p = "Casa comum"
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                    // TODO Auto-generated method stub
                }

            }

        transacao.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>, view: View,
                    position: Int, id: Long
                ) {
                    if (position == 0) {
                        option = "Alugar"
                    } else {
                        option = "Vender"
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                    // TODO Auto-generated method stub
                }
            }
        next_button.setOnClickListener {
            if (selected != null || selectPhotoUri != null || multiple) {
                if (position_p != "") {
                    if (option != null) {
                        if (txt_phone.text.toString() != "") {
                            if (txt_price.text.toString() != "") {
                                if (txt_cep.text.toString() != "") {
                                    if (txt_address.text.toString() != "") {
                                        if (txt_burgh.text.toString() != "") {
                                            if (txt_complement2.text.toString() != "") {
                                                inicializeFirebase()
                                                uploadImageToFirebase()
                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    "Adicione o número do imóvel!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Adicione o bairro do imóvel!",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }

                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Adicione o endereço do imóvel!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Adicione o CEP do imóvel!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Adicione um preço ao imóvel!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Escolha um telefone para contato!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }


                    } else {
                        Toast.makeText(
                            this,
                            "Escolha o tipo do imóvel!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Escolha a opção de negócio!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Adicione uma foto!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun inicializeFirebase() {
        FirebaseApp.initializeApp(this@EditActivity)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        multiple = false
        imageUtil.clear()
        selectPhotoUri = null
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                var slideList = arrayListOf<Slide>()
                val slider = image
                if (data!!.getClipData() != null) {
                    multiple = true
                    count = data.getClipData()!!.getItemCount()
                    count1 = count
                    var currentItem = 0
                    while (currentItem < count) {
                        var imagemUri = data.getClipData()!!.getItemAt(currentItem).getUri()
                        slideList.add(
                            Slide(
                                count,
                                imagemUri.toString(),
                                resources.getDimensionPixelSize(R.dimen.slider_image_corner)
                            )
                        )
                        imageUtil.add(imagemUri)
                        currentItem = currentItem + 1
                    }
                    slider.addSlides(slideList)
                } else if (data.getData() != null) {
                    multiple = false
                    selectPhotoUri = data.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectPhotoUri)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    /*Picasso.get().load(selectPhotoUri).resize(400, 100)
                        .centerCrop().into(image)

                     */

                    val slider = image

                    val slideList = arrayListOf<Slide>()
                    slideList.add(
                        Slide(
                            0,
                            selectPhotoUri.toString(),
                            resources.getDimensionPixelSize(R.dimen.slider_image_corner)
                        )
                    )

                    slider.addSlides(slideList)

                }

            }
            /*StorageReference firebaseRef = storageReference.child( storageReference +imagemUri.getLastPathSegment());
            StorageTask<UploadTask.TaskSnapshot> uploadTask = firebaseRef.putFile(imagemUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(TelaverGaleriaActivity.this, "Erro ao enviar imagens", Toast.LENGTH_SHORT).show();
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Toast.makeText(TelaverGaleriaActivity.this, "Upload com sucesso", Toast.LENGTH_SHORT).show();

                }
            });
        }

             */

        }

        /*
                                  CODIGO ANTIGO

         if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
             selectPhotoUri = data.data
             val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectPhotoUri)
             val baos = ByteArrayOutputStream()
             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
             Picasso.get().load(selectPhotoUri).resize(400, 100)
                 .centerCrop().into(image)
             // image1.alpha = 0f
         }

         */


    }

    var property = com.br.homes.model.Property()
    fun newProperty() {
        val authID = auth.uid
        property.uid = authID
        property.phone = txt_phone.text.toString()
        property.price = txt_price.text.toString()
        property.cep = txt_cep.text.toString()
        property.address = txt_address.text.toString()
        property.complement = txt_complement.text.toString()
        property.burgh = txt_burgh.text.toString()
        property.description = txt_description.text.toString()
        property.type = position_p
        property.random = randomID
        property.option = option
        property.number = txt_complement2.text.toString()

        if (!multiple) {
            property.url = downloadUri.toString()
            databaseReference!!.child("PropertyUser/").child(authID.toString()).child(randomID)
                .setValue(property)
            databaseReference!!.child("PropertyAll/").child(randomID).setValue(property)

        } else {

            property.url = randomPath
            databaseReference!!.child("PropertyUser/").child(authID.toString()).child(randomID)
                .setValue(property)
            databaseReference!!.child("PropertyAll/").child(randomID).setValue(property)


        }
    }

    fun uploadImageToFirebase() {

        val progressDialog = ProgressDialog.show(this, null, null);
        progressDialog.setContentView(R.layout.loader)
        progressDialog.window!!.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        progressDialog.setCancelable(false)

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference

        var database = FirebaseDatabase.getInstance()
        var reference = database.getReference().child("Imagens").child(randomID)

        if (!multiple) {
            val ref = storageReference.child("/property/").child(randomID)
            ref.putFile(selectPhotoUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            downloadUri = task.result!!
                            newProperty()
                            progressDialog.dismiss()
                            val i = Intent(
                                this@EditActivity,
                                SplashActivity::class.java
                            )
                            startActivity(i)
                        }
                    }
                }
        } else {
            count--
            randomPath = UUID.randomUUID().toString()
            while(count >= 0) {
                var rand = UUID.randomUUID()
                val ref = storageReference.child("/property/").child(randomPath)
                ref.putFile(imageUtil.get(count))
                    .addOnSuccessListener {
                        ref.downloadUrl.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                downloadUri = task.result!!
                                reference.child(rand.toString()).setValue(downloadUri.toString())
                            }
                        }
                    }
                count--
            }
            newProperty()
            progressDialog.dismiss()
            val i = Intent(
                this@EditActivity,
                SplashActivity::class.java
            )
            startActivity(i)
        }
    }
    fun searchCEP() {
        val cep = findViewById(R.id.txt_cep) as EditText
        val progressDialog = ProgressDialog(this@EditActivity)
        progressDialog.setTitle("Buscando CEP ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val call =
            RetrofitInitializer().apiRetrofitService().getEnderecoByCEP(cep.text.toString())

        call.enqueue(object : retrofit2.Callback<CEP> {

            override fun onResponse(call: retrofit2.Call<CEP>, response: retrofit2.Response<CEP>) {
                response.let {

                    val CEPs: CEP? = it.body()

                    val address = findViewById<TextView>(R.id.txt_address)
                    address.text = CEPs!!.logradouro.toString()
                    val burgh = findViewById<TextView>(R.id.txt_burgh)
                    burgh.text = CEPs.bairro.toString()

                    progressDialog.dismiss()

                    Log.i("CEP", CEPs.toString())
                }
            }

            override fun onFailure(call: retrofit2.Call<CEP>, t: Throwable) {
                progressDialog.dismiss()
                println("Erro $ t.message")
            }
        })
    }
    fun setValues() {

        val slider = image

        val slideList = arrayListOf<Slide>()
        slideList.add(
            Slide(
                0,
                propertyUtilAll.get(0).url,
                resources.getDimensionPixelSize(R.dimen.slider_image_corner)
            )
        )

        slider.addSlides(slideList)

        var phone = findViewById<TextView>(R.id.txt_phone)
        var price = findViewById<TextView>(R.id.txt_price)
        var complement2 = findViewById<TextView>(R.id.txt_complement2)
        var address= findViewById<TextView>(R.id.txt_address)
        var burgh= findViewById<TextView>(R.id.txt_burgh)
        var cep= findViewById<TextView>(R.id.txt_cep)
        var description = findViewById<TextView>(R.id.txt_description)
        var complement = findViewById<TextView>(R.id.txt_complement)


        phone.text = propertyUtilAll.get(0).phone
        price.text = propertyUtilAll.get(0).price
        complement2.text = propertyUtilAll.get(0).number
        address.text = propertyUtilAll.get(0).address
        burgh.text = propertyUtilAll.get(0).burgh
        cep.text = propertyUtilAll.get(0).cep
        description.text = propertyUtilAll.get(0).description
        complement.text = propertyUtilAll.get(0).complement
    }

}

