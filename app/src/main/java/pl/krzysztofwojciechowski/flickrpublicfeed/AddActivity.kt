package pl.krzysztofwojciechowski.flickrpublicfeed

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*

class AddActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        fpf_add_edit_date.setText(ADD_DATE_FORMAT.format(year, month, dayOfMonth))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val today = Calendar.getInstance()
        fpf_add_edit_date.setText(formatDate(today))
        fpf_add_button_save.setOnClickListener {
            val data = Intent()
            data.putExtra(INTENTEXTRA_IMAGE_URL, fpf_add_edit_image_url.text.toString())
            data.putExtra(INTENTEXTRA_NAME, fpf_add_edit_name.text.toString())
            data.putExtra(INTENTEXTRA_DATE, fpf_add_edit_date.text.toString())
            data.putExtra(INTENTEXTRA_TAGS, fpf_add_edit_tags.text.toString())
            setResult(RESULT_OK, data)
            finish()
        }
        fpf_add_edit_date.setOnClickListener {
            DatePickerFragment().show(supportFragmentManager, TAG_DATEPICKER)
        }
        fpf_add_button_set_date.setOnClickListener {
            DatePickerFragment().show(supportFragmentManager, TAG_DATEPICKER)
        }
    }
}
