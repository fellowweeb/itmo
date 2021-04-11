package kt.fellowweeb.contacts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contact_layout_horizontal.view.*
import kotlinx.android.synthetic.main.contact_layout_vertical.view.*
import java.util.*
import kotlin.collections.ArrayList


data class Contact(val name: String, val phoneNumber: String)

fun Context.fetchAllContacts(): List<Contact> {
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )
        .use { cursor ->
            if (cursor == null) return emptyList()
            val builder = ArrayList<Contact>()
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        ?: getString(
                            R.string.not_available
                        )
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        ?: getString(
                            R.string.not_available
                        )

                builder.add(
                    Contact(
                        name,
                        PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().country)
                    )
                )
            }
            return builder
        }
}

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_READ_CONTACTS_REQUEST_CODE = 1
        const val COLUMNS_COUNT = 2
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ContactAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = GridLayoutManager(this, COLUMNS_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isContactViewVertical(position)) {
                        1
                    } else {
                        COLUMNS_COUNT
                    }
                }
            }
        }
        viewAdapter = ContactAdapter(emptyList(),
            {
                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.phoneNumber}"))
                startActivity(dialIntent)
            },
            {
                val sendIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("sms:${it.phoneNumber}"))
                sendIntent.putExtra("sms_body", "${it.name}, ")
                startActivity(sendIntent)
            }
        )

        recyclerView = recyclerview.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            setHasFixedSize(true)
        }

        tryContactsListShow()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.contactsUpdate -> {
            tryContactsListShow()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun tryContactsListShow() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION_READ_CONTACTS_REQUEST_CODE
            )
        } else {
            contactsListShow()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_READ_CONTACTS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults.first() == PackageManager.PERMISSION_GRANTED
                ) {
                    contactsListShow()
                } else {
                    Toast
                        .makeText(
                            this,
                            getString(R.string.contactsReadPermissionNotGranted),
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            }
        }
    }

    private fun contactsListShow() {
        val contacts = fetchAllContacts().sortedBy { it.name }.distinct()

        val contactsFoundText = resources.getQuantityString(
            R.plurals.contactsFound,
            contacts.size,
            contacts.size
        )

        viewAdapter.contacts = contacts

        Toast.makeText(this, contactsFoundText, Toast.LENGTH_SHORT).show()
    }
}

class ContactAdapter(
    contacts: List<Contact>,
    private val onClick: (Contact) -> Unit,
    private val onButtonClick: (Contact) -> Unit
) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    var contacts: List<Contact> = contacts
        set(list) {
            val diffResult = DiffUtil.calculateDiff(ContactDiffUtilCallback(contacts, list), false)
            field = list
            diffResult.dispatchUpdatesTo(this)
        }

    abstract class ContactViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        abstract fun bind(contact: Contact)
    }

    class ContactHorizontalViewHolder(root: View) : ContactViewHolder(root) {
        override fun bind(contact: Contact) {
            root.contact_horizontal_name.text = contact.name
            root.contact_horizontal_number.text = contact.phoneNumber
        }
    }

    class ContactVerticalViewHolder(root: View) : ContactViewHolder(root) {
        override fun bind(contact: Contact) {
            root.contact_vertical_name.text = contact.name
            root.contact_vertical_number.text = contact.phoneNumber
        }
    }

    override fun getItemViewType(position: Int): Int = if (isContactViewVertical(position)) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        fun func(layout: Int, build: (View) -> ContactViewHolder): ContactViewHolder {
            val cardView = LayoutInflater
                .from(parent.context)
                .inflate(
                    layout,
                    parent,
                    false
                )
            val holder: ContactViewHolder = build(cardView)
            return holder.apply {
                root.setOnClickListener {
                    onClick(contacts[holder.adapterPosition])
                }
            }
        }
        return if (viewType == 0) {
            func(R.layout.contact_layout_vertical, ::ContactVerticalViewHolder).apply {
                root.contact_vertical_sendImageButton.setOnClickListener {
                    onButtonClick(contacts[adapterPosition])
                }
            }
        } else {
            func(R.layout.contact_layout_horizontal, ::ContactHorizontalViewHolder).apply {
                root.contact_horizontal_sendImageButton.setOnClickListener {
                    onButtonClick(contacts[adapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size
}

class ContactDiffUtilCallback(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true
}

private fun isContactViewVertical(position: Int): Boolean = (position + 1) % (MainActivity.COLUMNS_COUNT + 1) != 0